package com.thundr.audience


import org.apache.spark.sql.DataFrame

case class AudienceTransferSpec[T](
                                 params: T,
                                 naming_convention: T => String,
                                 audience_query_template: T => DataFrame,
                                 datasource_codes: List[String],
                                 dac_id: T => String
                               ) {
  def name: String = naming_convention(params)

  def seed: DataFrame = audience_query_template(params)

  def toSeededInitialTransfer: ImpAudienceInitialTransferSeeded = {
    ImpAudienceInitialTransferSeeded(name = this.name, seed = seed, data_sources = datasource_codes)
  }

  def toSeededRefresh: ImpAudienceRefreshSeeded = {
    ImpAudienceRefreshSeeded(name =  this.name, seed = this.seed, dac_id = dac_id(params), data_sources = datasource_codes)
  }

  def execute_initial_transfer: ImpAudienceDAC = {
    toSeededInitialTransfer
      .createOrReplaceInCatalogue
      .persistXfer
      .activateToDiscovery()
  }

  def execute_refresh: ImpAudienceDAC = {
    toSeededRefresh
      .upsertInCatalogue
      .persistXfer()
      .refreshInDiscovery()
  }
}
