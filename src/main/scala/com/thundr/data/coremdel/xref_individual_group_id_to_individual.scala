package com.thundr.data.coremdel

import com.thundr.data.DataSource
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions._

case object xref_individual_group_id_to_individual
  extends DataSource {
  override def name: String = "xref_individual_group_id_to_individual"

  override def namespace: String = "coremodel"

  override def prefix: String = customer_prefix

  override def dimensionalized: DataFrame = this.read.as(name)
    .join(
      dim_individual_group_type.dimensionalized.as(dim_individual_group_type.name),
      col(s"${dim_individual_group_type.name}.individual_group_type_id") === col(s"${this.name}.individual_group_type_id"),
//      dim_individual_group_type("individual_group_type_id") === this("individual_group_type_id"),
      "left"
    )
}
