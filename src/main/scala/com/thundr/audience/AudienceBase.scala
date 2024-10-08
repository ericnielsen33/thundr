package com.thundr.audience


import com.thundr.config.{ConfigProvider, SessionProvider}
import com.thundr.core.services.audience_catalogue.AudienceCatalogueProvider
import com.thundr.core.services.audience_lifeycle.AudienceLifecycleProvider
import org.apache.spark.sql.{Column, DataFrame}
import org.apache.spark.sql.functions._

abstract class AudienceBase
  extends SessionProvider with ConfigProvider {

  def audienceCatalogueProvider: AudienceCatalogueProvider = new AudienceCatalogueProvider(session)
  def audienceLifecycleProvider: AudienceLifecycleProvider = new AudienceLifecycleProvider(session)
  def audienceStatusProvider: AudienceStatusProvider = new AudienceStatusProvider(session)

  def apply(colName: String): Column = col(s"$audience_name.$colName")

  def xfer_location: String = s"${customer_prefix}.audience_xfer.${audience_name.toLowerCase()}".trim()

  def xfer_path: String = s"${audeince_xfer_root}/${audience_name}.csv".trim()

  def id_col_alias: String = "individual_identity_key"

  def id: Column = col(id_col_alias)

  def audience_name: String

  def read: DataFrame

  def withAlias: DataFrame = read.as(audience_name)

  def union(other: AudienceBase): AudienceBase = {
    val unioned = this.read.select(id)
      .union(other.read.select(other.id))
    val newName = this.audience_name + "_Union_" + other.audience_name
    ImpAudienceInitialTransferSeeded(newName, unioned)
  }

  def intersect(other: AudienceBase) : AudienceBase = {
    val intersected = this.withAlias
      .join(other.withAlias, this.id.equalTo(other.id), "inner")
    val newName = this.audience_name + "_Intersection_" + other.audience_name
    ImpAudienceInitialTransferSeeded(newName, intersected)
  }

  def supress(other: AudienceBase): AudienceBase = {
      val supressed = this.withAlias
        .join(other.withAlias, this.id.equalTo(other.id), "leftanti")
      val newName = this.audience_name + "_supression_" + other.audience_name
      ImpAudienceInitialTransferSeeded(newName, supressed)
  }
}
