package com.thundr.audience


import com.thundr.core.services.audience_lifeycle.{AudienceLifecycleProvider, AudienceLifecycleSchema}

import java.sql.Timestamp
import org.apache.spark.sql.DataFrame

case class ImpAudienceRefreshSeeded(name: String, seed: DataFrame, dac_id: String, data_sources: List[String] = List(), brands: List[String] = List())
  extends AudienceBase {

  override def audience_name: String = name

  override def read: DataFrame = seed

  def upsertInCatalogue: ImpAudienceRefreshCatalogued = {
    val event: AudienceLifecycleSchema = AudienceLifecycleSchema(
      name,
      new Timestamp(System.currentTimeMillis()),
      "UPSERT", None, None)

    audienceCatalogueProvider.merge(name, seed)
    AudienceLifecycleProvider.append(event)
    ImpAudienceRefreshCatalogued(name = name, dac_id = dac_id, data_sources = data_sources)
  }

}
