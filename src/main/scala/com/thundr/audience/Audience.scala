package com.thundr.audience

import com.thundr.config.{ConfigProvider, SessionProvider}
import org.apache.spark.sql.{Column, DataFrame, SaveMode, SparkSession}
import org.apache.spark.sql.functions._

import java.sql.Timestamp

//consider having an audience class w/ name only (no df seed) for easy use of many utility methods.
//consider creating audience manager that can use metadata to manage lifecyle of all audiences.
//consider creating abstact audience class, then returning subtaypes
// basted off of adding a seed, creating it, persisting to xfer / activating it.

case class Audience( seed: DataFrame,
                     name: String,
                     dac_id: String = "",
                     id: String = "individual_identity_key")
  extends ConfigProvider with SessionProvider {

  private val audienceCatalogueProvider: AudienceCatalogueProvider = new AudienceCatalogueProvider(session)
  private val audienceLifecycleProvider: AudienceLifecycleProvider = new AudienceLifecycleProvider(session)
  private val audienceStatusProvider: AudienceStatusProvider = new AudienceStatusProvider(session)
  val xfer_location: String = s"${customer_prefix}.audience_xfer.${name.toLowerCase()}".trim()
  val xfer_path: String = s"${audeince_xfer_root}/${name}.csv".trim()

  def withAlias: DataFrame = this.seed.as(this.name)
  def apply(colName: String): Column = col(s"$name.$colName")
  def readFromCatalogue(): DataFrame = audienceCatalogueProvider.readAudinece(this)
  def contains(other: Audience): Boolean = {
    this.withAlias
      .join(other.withAlias, this(id) === other(id), "left")
      .agg(max(other(id).isNull))
      .collectAsList()
      .get(0)
      .getBoolean(0)
  }
  def audienceCount: Int = readFromCatalogue().count().toInt

  def overlaps(other: Audience): Boolean = {
    this.withAlias
      .join(other.withAlias, this(id) === other(id), "left")
      .agg(max(other(id).isNotNull))
      .collectAsList()
      .get(0)
      .getBoolean(0)
  }

  def overlap(other: Audience): Int = {
    this.withAlias
      .join(other.withAlias, this (id) === other(id), "left")
      .agg(count(other(id).isNotNull))
      .collectAsList()
      .get(0)
      .getInt(0)
  }
  def union(other: Audience): Audience = {
    val audienceUnion = this.seed
        .select(col(this.id))
        .union(other.seed.select(col(other.id).as(this.id)))
        .distinct()
    Audience(audienceUnion, name + "_u_" + other.name)
  }
  def intersection(other: Audience): Audience = {
    val audienceIntersection =
      this.seed
        .select(col(this.id))
        .intersect(other.seed.select(col(other.id).as(this.id)))
    Audience(audienceIntersection, name + "_i_" + other.name)
  }
  def difference(other: Audience): Audience = {
    val audienceDifference =
      this.seed
        .select(col(this.id))
        .except(other.seed.select(col(other.id).as(this.id)))
    Audience(audienceDifference, name + "_d_" + other.name)
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
    None,
    Option(upickle.default.write(Map("xfer_location" -> xfer_location))))) = {
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
    val response = DacClient.postNewAudience(name, xfer_location)
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
    Audience(seed, name, dac_id_res, id)
  }
  def pollStatus(): DacPollResponse = {
    val pollResponse = DacClient.pollAudienceStatus(this.dac_id)
    audienceStatusProvider.append(pollResponse)
    pollResponse
  }
  def dropXferTable(): Unit = {
    val event: AudienceLifecycleSchema = AudienceLifecycleSchema(
      name,
      new Timestamp(System.currentTimeMillis()),
      "DROP_XFER_TABLE",
      None,
      Option(upickle.default.write(Map("xfer_location" -> xfer_location))))
    session.sql(s"DROP TABLE IF EXISTS ${xfer_location}")
    audienceLifecycleProvider.append(event)
  }
  def withName(newName: String): Audience = Audience(seed, newName, dac_id, id)

  def withPersistedDacId(): Audience = {
    val hist_dac_id = audienceLifecycleProvider.getHistory(this)
      .filter(_.event.equals("TRANSFER_OK"))
      .head
      .props_json
      .getOrElse(upickle.default.write(Map("dac_id" -> "n/a")).toString)
    val new_dac_id = ujson.read(hist_dac_id).str
    Audience(seed, name, new_dac_id, id)
  }
  def deleteAudience() = {
    val event: AudienceLifecycleSchema = AudienceLifecycleSchema(name, new Timestamp(System.currentTimeMillis()), "DELETE", None, None)
    audienceCatalogueProvider.deleteAudience(this)
    audienceLifecycleProvider.append(event)
  }
}
