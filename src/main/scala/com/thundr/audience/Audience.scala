package com.thundr.audience

import com.thundr.config.{ConfigProvider, SessionProvider}
import org.apache.spark.sql.{Column, DataFrame, SaveMode}
import org.apache.spark.sql.functions._
import com.thundr.audience.{DacClientV3 => DacClient }
import com.thundr.data.coremodel.xref_individual_group_id_to_individual
import java.sql.Timestamp
import org.json4s.jackson.Serialization

//consider having an audience class w/ name only (no df seed) for easy use of many utility methods.
//consider creating audience manager that can use metadata to manage lifecyle of all audiences.
//consider creating abstact audience class, then returning subtaypes
// basted off of adding a seed, creating it, persisting to xfer / activating it.

case class Audience( seed: DataFrame,
                     name: String,
                     dac_id: String = "",
                     id: String = "individual_identity_key",
                     data_sources: List[String] = List.empty)
  extends ConfigProvider with SessionProvider {
  implicit val formats = org.json4s.DefaultFormats
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
  def audienceCatalogueCount: Int = readFromCatalogue().count().toInt

  def householdCount: Int = readFromCatalogue().as("this")
    .join(
      xref_individual_group_id_to_individual.dimensionalized,
      xref_individual_group_id_to_individual.individual_identity_key
        .equalTo("this.individual_identity_key") &&
        xref_individual_group_id_to_individual("individual_group_type_name").equalTo("TSP HH"),
      "left")
    .count().toInt

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

  def persistXfer()  = {
    val data: Map[String, String] =  Map("xfer_location" -> xfer_location)
    val json: String = Serialization.write(data)
    val event = AudienceLifecycleSchema(
      name,
      new Timestamp(System.currentTimeMillis()),
      "PERSIST_XFER",
      None,
      Option(json))
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
    val response: String = DacClient.postNewAudience(name, xfer_location, data_sources)
    val decoded = DacPostNewAudienceResponse.decode(response)

    val data: Map[String, String] =  Map("dac_id" -> decoded.dac_id)
    val json: String = Serialization.write(data)
    val event: AudienceLifecycleSchema = AudienceLifecycleSchema(
      name,
      new Timestamp(System.currentTimeMillis()),
      "TRANSFER_OK",
      None,
      Option(json)
    )
    audienceLifecycleProvider.append(event)
    Audience(seed, name, decoded.dac_id, id)
  }
  def pollStatus(): DacPollResponse = {
    val response: DacPollResponse = DacClient.pollAudienceStatus(this.dac_id)
    audienceStatusProvider.append(response)
    response
  }
  def dropXferTable(): Unit = {
    val data: Map[String, String] =  Map("xfer_location" -> xfer_location)
    val json: String = Serialization.write(data)
    val event: AudienceLifecycleSchema = AudienceLifecycleSchema(
      name,
      new Timestamp(System.currentTimeMillis()),
      "DROP_XFER_TABLE",
      None,
      Option(json)
    )
    session.sql(s"DROP TABLE IF EXISTS ${xfer_location}")
    audienceLifecycleProvider.append(event)
  }
  def withName(newName: String): Audience = Audience(seed, newName, dac_id, id)
  def withDacId(new_dac_id: String): Audience = Audience(seed, name, new_dac_id, id)

  def withPersistedDacId(): Audience = {
//    val hist_dac_id = audienceLifecycleProvider.getHistory(this)
//      .filter(_.event.equals("TRANSFER_OK"))
//      .head
//      .props_json
//      .getOrElse(write(Map("dac_id" -> "n/a")))
    val new_dac_id = "TODO"
    Audience(seed, name, new_dac_id, id)
  }
  def deleteAudience() = {
    val event: AudienceLifecycleSchema = AudienceLifecycleSchema(name, new Timestamp(System.currentTimeMillis()), "DELETE", None, None)
    audienceCatalogueProvider.deleteAudience(this)
    audienceLifecycleProvider.append(event)
  }
}
