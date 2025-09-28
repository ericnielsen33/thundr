package com.thundr.audience

import java.sql.Timestamp
import org.apache.spark.sql.DataFrame
import com.thundr.core.services.audience_lifeycle.{AudienceLifecycleProvider, AudienceLifecycleSchema}
import com.thundr.core.services.dac.{DacClientV3 => DacClient}
import org.json4s.DefaultFormats
import org.json4s.jackson.Serialization


case class ImpAudienceInitialTransferStagedToXfer(name: String, location: String, data_sources: List[String] = List(), brands: List[String] = List())
  extends AudienceBase {

  override def read: DataFrame = session.read.table(location)

  override def audience_name: String = name

  def activateToDiscovery(): ImpAudienceDAC = {
    implicit val formats: DefaultFormats = DefaultFormats
    val response: String = DacClient.postNewAudience(name, location, data_sources, brands)
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
    AudienceLifecycleProvider.append(event)

    ImpAudienceDAC(name, decoded.dac_id, data_sources)
  }

  def delete(): ImpAudienceInitialTransferCatalogued = ???

  }
