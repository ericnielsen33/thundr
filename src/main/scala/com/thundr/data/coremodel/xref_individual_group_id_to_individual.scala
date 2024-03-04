package com.thundr.data.coremodel

import com.thundr.data.DataSource
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions._

case object xref_individual_group_id_to_individual
  extends DataSource {
  override def name: String = "xref_individual_group_id_to_individual"

  override def namespace: String = "coremodel"

  override def prefix: String = customer_prefix

  override def dimensionalized: DataFrame = this.withAlias
    .join(
      dim_individual_group_type.dimensionalized,
      dim_individual_group_type("individual_group_type_id") === this("individual_group_type_id"),
      "left"
    )
}
