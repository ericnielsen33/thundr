package com.thundr.audience

import com.thundr.core.services.dac.{DacClientV3 => DacClient}
import org.apache.spark.sql.DataFrame

case class ImpAudienceDAC(name: String, dac_id: String, data_sources: List[String] = List())
  extends AudienceBase {

  override def read: DataFrame = audienceCatalogueProvider.readAudience(name)

  override def audience_name: String = name

  def pollStatus: DacPollResponse = {
    val response: DacPollResponse = DacClient.pollAudienceStatus(this.dac_id)
    audienceStatusProvider.append(response)
    response
  }
}
