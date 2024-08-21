package com.thundr.audience

import org.apache.spark.sql.DataFrame
import com.thundr.audience.{DacClientV3 => DacClient }

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
