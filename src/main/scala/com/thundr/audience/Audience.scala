package com.thundr.audience

import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}
import org.apache.spark.sql.functions._
import java.sql.Timestamp


case class Audience(
                     session: SparkSession,
                     seed: DataFrame,
                     name: String,
                     dac_id: String = "",
                     id: String = "individual_identity_key") {

  private val leftAlias: String = "left"
  private val rightAlias: String = "right"
  lazy val customer_prefix: String = scala.util.Properties.envOrNone("CUSTOMER_PREFIX").get
  lazy val audeince_xfer_root: String = scala.util.Properties.envOrNone("AUDIENCE_XFER_ROOT").get
  private val audienceCatalogueProvider: AudienceCatalogueProvider = new AudienceCatalogueProvider(session)
  private val audienceLifecycleProvider: AudienceLifecycleProvider = new AudienceLifecycleProvider(session)
  private val audienceStatusProvider: AudienceStatusProvider = new AudienceStatusProvider(session)
  val xfer_location: String = s"${customer_prefix}.audience_xfer.${name.toLowerCase()}"
  val xfer_path: String = s"${audeince_xfer_root}/${name}.csv"

  def apply(tasks: (Audience => Unit)*): Unit = tasks.foreach(_.apply(this))
  def readFromCatalogue(): DataFrame = audienceCatalogueProvider.readAudinece(this)
  def contains(other: Audience): Boolean = {
    this.seed.as(leftAlias)
      .join(
        other.seed.as(rightAlias),
        col(s"${leftAlias}.${id}") === col(s"${rightAlias}.${other.id}"),
        "left")
      .agg(max(col(s"${rightAlias}.${other.id}").isNull))
      .collectAsList()
      .get(0)
      .getBoolean(0)
  }
  def audienceCount: Int = readFromCatalogue().count().toInt

  def overlaps(other: Audience): Boolean = {
    this.seed.as(leftAlias)
      .join(
        other.seed.as(rightAlias),
        col(s"${leftAlias}.${id}") === col(s"${rightAlias}.${other.id}"),
        "left")
      .agg(max(col(s"${rightAlias}.${other.id}").isNotNull))
      .collectAsList()
      .get(0)
      .getBoolean(0)
  }

  def overlap(other: Audience): Int = {
    this.seed.as(leftAlias)
      .join(
        other.seed.as(rightAlias),
        col(s"${leftAlias}.${id}") === col(s"${rightAlias}.${other.id}"),
        "left")
      .agg(count(col(s"${rightAlias}.${other.id}").isNotNull))
      .collectAsList()
      .get(0)
      .getInt(0)
  }
  def union(other: Audience): Audience = {
    val audienceUnion = this.seed
        .select(col(this.id))
        .union(other.seed.select(col(other.id).as(this.id)))
        .distinct()
    Audience(this.session, audienceUnion, name + "_u_" + other.name)
  }
  def intersection(other: Audience): Audience = {
    val audienceIntersection =
      this.seed
        .select(col(this.id))
        .intersect(other.seed.select(col(other.id).as(this.id)))
    Audience(this.session, audienceIntersection, name + "_i_" + other.name)
  }
  def difference(other: Audience): Audience = {
    val audienceDifference =
      this.seed
        .select(col(this.id))
        .except(other.seed.select(col(other.id).as(this.id)))
    Audience(this.session, audienceDifference, name + "_d_" + other.name)
  }
  def create(): Unit = {
    val event: AudienceLifecycleSchema = AudienceLifecycleSchema(name,
      new Timestamp(System.currentTimeMillis()), "CREATE", None, None)
    audienceCatalogueProvider.insertNewAudience(this)
    audienceLifecycleProvider.append(event)
  }

  def persistXfer(event:AudienceLifecycleSchema = AudienceLifecycleSchema(
    name,
    new Timestamp(System.currentTimeMillis()),
    "PERSIST_XFER",
    None, None)) = {
    this.readFromCatalogue.select(col("individual_identity_key")).distinct()
      .write
      .mode(SaveMode.Overwrite)
      .format("csv")
      .option("path", xfer_path)
      .option("headers", "true")
      .saveAsTable(xfer_location)
    audienceLifecycleProvider.append(event)
  }
  def activateToDiscovery(): Audience = {
    val response = DacClient.postNewAudience(this.name)
    val json = ujson.read(response)
    val dac_id_res = json("dac_id").str
    val event: AudienceLifecycleSchema = AudienceLifecycleSchema(
      name,
      new Timestamp(System.currentTimeMillis()),
      "TRANSFER_OK",
      None,
      Option(upickle.default.write(Map("dac_id" -> dac_id_res)))
    )
    audienceLifecycleProvider.append(event)
    Audience(session, seed, name, dac_id_res, id)
  }
  def pollStatus(): DacPollResponse = {
    val pollResponse = DacClient.pollAudienceStatus(this.dac_id)
    audienceStatusProvider.append(pollResponse)
    pollResponse
  }
  def dropXferTable(): Unit = {
    val event: AudienceLifecycleSchema = AudienceLifecycleSchema(name, new Timestamp(System.currentTimeMillis()), "DROP_XFER_TABLE", None, None)
    session.sql(s"DROP TABLE IF EXISTS ${xfer_location}")
    audienceLifecycleProvider.append(event)
  }
  def withName(newName: String): Audience = Audience(session, seed, newName, dac_id, id)

  def withPersistedDacId(): Audience = {
    val hist_dac_id = audienceLifecycleProvider.getHistory(this)
      .filter(_.event.equals("TRANSFER_OK"))
      .head
      .props_json
      .getOrElse(upickle.default.write(Map("dac_id" -> "n/a")).toString)
    val new_dac_id = ujson.read(hist_dac_id).str
    Audience(session, seed, name, new_dac_id, id)
  }
  def deleteAudience() = audienceCatalogueProvider.deleteAudience(this)
}
