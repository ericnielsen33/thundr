package com.thundr.audience

import java.sql.Timestamp
import org.apache.spark.sql.DataFrame

 case class ImpAudienceInitialTransferSeeded(name: String, seed: DataFrame, data_sources: List[String] = List())
  extends AudienceBase {

  override def read: DataFrame = seed

  override def audience_name: String = name

  def createOrReplaceInCatalogue: ImpAudienceInitialTransferCatalogued = {

   val event: AudienceLifecycleSchema = AudienceLifecycleSchema(
    name,
    new Timestamp(System.currentTimeMillis()),
    "CREATE", None, None)

   audienceCatalogueProvider.insertNewAudience(name, seed)
   audienceLifecycleProvider.append(event)
   ImpAudienceInitialTransferCatalogued(name, data_sources = data_sources )
  }

  def upsertInCatalogue: ImpAudienceInitialTransferCatalogued = {
   val event: AudienceLifecycleSchema = AudienceLifecycleSchema(
    name,
    new Timestamp(System.currentTimeMillis()),
    "UPSERT", None, None)

   audienceCatalogueProvider.merge(name, seed)
   ImpAudienceInitialTransferCatalogued(name = name, data_sources = data_sources)
  }
}
