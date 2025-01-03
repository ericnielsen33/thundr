package com.thundr.audience

import org.apache.spark.sql.DataFrame
import com.thundr.core.services.audience_lifeycle.AudienceLifecycleSchema
import com.thundr.core.services.dac.{DacClientV3 => DacClient}

import java.sql.Timestamp
import org.json4s.jackson.Serialization

case class ImpAudienceRefreshStagedToXfer(name: String, location: String, dac_id: String, data_sources: List[String] = List())
  extends AudienceBase {

  override def read: DataFrame = session.read.table(location)

  override def audience_name: String = name

//  need to decode response and decide if more should be added into json field
  def refreshInDiscovery(): ImpAudienceDAC = {
    implicit val formats = org.json4s.DefaultFormats
    val response: String = DacClient.refreshExistingAudience(name, location, dac_id, data_sources)
    val decoded = DacPostNewAudienceResponse.decode(response)
    val data: Map[String, String] =  Map("dac_id" -> decoded.dac_id)
    val json: String = Serialization.write(data)

    val event: AudienceLifecycleSchema = AudienceLifecycleSchema(
      name,
      new Timestamp(System.currentTimeMillis()),
      "REFRESH_OK",
      None,
      Option(json)
    )

    audienceLifecycleProvider.append(event)

    ImpAudienceDAC(name, decoded.dac_id, data_sources)
  }

  def delete() : ImpAudienceRefreshCatalogued = ???

}
