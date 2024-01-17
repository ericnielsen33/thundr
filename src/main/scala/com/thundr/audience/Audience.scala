package com.thundr.audience

import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}
import org.apache.spark.sql.functions._
import java.sql.Timestamp
import com.thundr.audience.DacClient

case class Audience(
                     session: SparkSession,
                     seed: DataFrame,
                     name: String,
                     dac_id: String = "",
                     id: String = "individual_identity_key") {

  private val leftAlias: String = "left"
  private val rightAlias: String = "right"
  private val audienceCatalogueProvider: AudienceCatalogueProvider = new AudienceCatalogueProvider(session = session)
  private val audienceMetaProvider: AudienceMetaProvider = new AudienceMetaProvider(session = session)
  private val audienceEventProvider: AudienceEventProvider = new AudienceEventProvider(session = session)

  def apply(other: Audience): Boolean = this.contains(other)

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

  def audienceCount: Int = {
    readFromCatalogue().count().toInt
  }

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

  def create(
              audienceMeta: AudienceMetaSchema,
              event:  AudienceEventSchema = AudienceEventSchema(this.name, new Timestamp(System.currentTimeMillis()), "CREATE")): Unit = {
    audienceCatalogueProvider.insertNewAudience(this)
    persistMetadata(audienceMeta)
    persistEvent(event)
  }

  def persistXfer() = seed
    .select(col(id).as("individual_identity_key"))
    .write
    .mode(SaveMode.Overwrite)
    .format("csv")
    .option("path", s"s3://pmx-prod-uc-us-east-1-databricks-iq/audience_xfer/${name}.csv")
    .option("headers", "true")
    .saveAsTable(s"p1pmx_prospect.audience_xfer.${name}")

  persistEvent(AudienceEventSchema(this.name, new Timestamp(System.currentTimeMillis()), "PERSIST_XFER"))
//  def activateToDiscovery(dac_token: String): Unit = AudienceDacClient.postNewAudience(defaultPrefix,this, dac_token)

  def persistMetadata(metaSchema: AudienceMetaSchema): Unit = audienceMetaProvider.append(metaSchema)

  def persistEvent(eventSchema: AudienceEventSchema): Unit = audienceEventProvider.append(eventSchema)

  def pollStatus(): String = DacClient.pollAudienceStatus(this.dac_id)
}
