package com.thundr.audience

import java.sql.Timestamp
import org.apache.spark.sql.{DataFrame, SaveMode}
import com.thundr.core.services.audience_lifeycle.{AudienceLifecycleProvider, AudienceLifecycleSchema}
import org.apache.spark.sql.functions._
import org.json4s.jackson.Serialization

case class ImpAudienceInitialTransferSCD1Catalogued(name: String, audience_id: String, data_sources: List[String])
  extends AudienceBase {


  val fact_audience_member_table_ref = s"$customer_prefix.audience_xfer.fact_audience_member"

  override def xfer_location: String = s"${customer_prefix}.audience_xfer.xfer_manager_${audience_id}".trim()

  override def read: DataFrame = session.table(fact_audience_member_table_ref)
    .filter(col("audience_id").equalTo(audience_id))

  override def audience_name: String = name

  def persistXfer: ImpAudienceInitialTransferStagedToXfer = {
    implicit val formats = org.json4s.DefaultFormats
    val data: Map[String, String] =  Map(
      "xfer_location" -> xfer_location,
      "audience_id" -> audience_id,
      "data_sources" -> data_sources.toString()
    )
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

    AudienceLifecycleProvider.append(event)

    ImpAudienceInitialTransferStagedToXfer(name = name, location = xfer_location , data_sources = data_sources)
  }

  def delete(): ImpAudienceInitialTransferSeeded = ???

}
