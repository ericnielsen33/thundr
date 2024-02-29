package com.thundr.data.coremdel

import com.thundr.data.DataSource
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions._

case object xref_individual_group_id_to_individual
  extends DataSource {
  override def name: String = "xref_individual_group_id_to_individual"

  override def namespace: String = "coremodel"

  override def prefix: String = customer_prefix

  def dimensionalized: DataFrame = this.read.as(name)
    .join(
      dim_individual_group_type.read.as(dim_individual_group_type.name),
      col(s"${name}.individual_group_type_id") === col(s"${dim_individual_group_type.name}.individual_group_type_id"),
      "left"
    )
}
