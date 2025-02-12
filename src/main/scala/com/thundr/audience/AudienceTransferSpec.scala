package com.thundr.audience


import org.apache.spark.sql.DataFrame

case class AudienceTransferSpec(
                                 params: Map[String, Any],
                                 naming_convention: Map[String, Any] => String,
                                 audience_query_template: Map[String, Any] => DataFrame,
                                 datasource_codes: List[String],
                                 meta: Option[Map[String, Any]]
                               ) {
  def name: String = naming_convention(params)

  def seed: DataFrame = audience_query_template(params)

  def toSeededInitialTransfer: ImpAudienceInitialTransferSeeded = {
    ImpAudienceInitialTransferSeeded(name = this.name, seed = seed, data_sources = datasource_codes)
  }

  def toSeededRefresh: ImpAudienceRefreshSeeded = {
    ImpAudienceRefreshSeeded(name =  this.name, seed = this.seed, dac_id = meta.get.get("dac_id").toString, data_sources = datasource_codes)
  }

}
