package com.thundr.audience


import com.thundr.core.services.audience_lifeycle.AudienceLifecycleSchema

import java.sql.Timestamp
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions._


case class ImpAudienceRefreshSeeded(name: String, seed: DataFrame, dac_id: String, data_sources: List[String] = List())
  extends AudienceBase {

  override def audience_name: String = name

  override def read: DataFrame = seed

//  needs to return a new Audience implementation that is refreshable and Catalogued
//  Catalogued version my need to be sure to only read the catalogue head
  def upsertInCatalogue: ImpAudienceRefreshCatalogued = {
    val event: AudienceLifecycleSchema = AudienceLifecycleSchema(
      name,
      new Timestamp(System.currentTimeMillis()),
      "UPSERT", None, None)

    audienceCatalogueProvider.merge(name, seed)
    ImpAudienceRefreshCatalogued(name = name, dac_id = dac_id, data_sources = data_sources)
  }

}
