package com.thundr.audience

import com.thundr.core.services.audience_lifeycle.AudienceLifecycleSchema
import org.apache.spark.sql.{DataFrame, SaveMode}
import org.apache.spark.sql.functions.col
import org.json4s.jackson.Serialization
import java.sql.Timestamp


case class ImpAudienceInitialTransferCatalogued(name: String, data_sources: List[String] = List())
  extends AudienceBase {

//  need to ensure audiences have a null end date in the catalogue.
//  might be best to wipe the tables to start?
  override def read: DataFrame = {
    val all_time: DataFrame = audienceCatalogueProvider.readAudience(name)
    val curr: DataFrame = all_time.filter(col("end_date").isNull)
    curr
  }

  override def audience_name: String = name


  def persistXfer: ImpAudienceInitialTransferStagedToXfer = {
    implicit val formats = org.json4s.DefaultFormats
    val data: Map[String, String] =  Map("xfer_location" -> xfer_location)
    val json: String = Serialization.write(data)

    val event = AudienceLifecycleSchema(
      name,
      new Timestamp(System.currentTimeMillis()),
      "PERSIST_XFER",
      None,
      Option(json))

    this.read.select(col("individual_identity_key")).distinct()
      .write
      .mode(SaveMode.Overwrite)
      .format("csv")
      .option("path", xfer_path)
      .option("headers", "true")
      .saveAsTable(xfer_location)

    audienceLifecycleProvider.append(event)

    ImpAudienceInitialTransferStagedToXfer(name = name, location = xfer_location , data_sources = data_sources)
  }

  def delete(): ImpAudienceInitialTransferSeeded = ???
}
