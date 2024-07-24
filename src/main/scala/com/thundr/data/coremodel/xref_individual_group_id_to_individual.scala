package com.thundr.data.coremodel

import com.thundr.data.{CoreID, DataSource}
import org.apache.spark.sql.{DataFrame, Column}
import org.apache.spark.sql.functions._

case object xref_individual_group_id_to_individual
  extends DataSource
  with CoreID {
  override def name: String = "xref_individual_group_id_to_individual"

  override def namespace: String = "coremodel"

  override def prefix: String = customer_prefix

  override def dimensionalized: DataFrame = this.withAlias
    .join(
      dim_individual_group_type.dimensionalized,
      dim_individual_group_type("individual_group_type_id") === this("individual_group_type_id"),
      "left"
    )
    .drop(dim_individual_group_type("individual_group_type_id") )
    .as(name)

  def individual_group_identity_key: Column = this("individual_group_identity_key")

  def individual_group_type_id: Column = this("individual_group_type_id")
}
