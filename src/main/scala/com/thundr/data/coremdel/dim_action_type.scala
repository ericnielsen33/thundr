package com.thundr.data.coremdel

import com.thundr.data.DataSource
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions._

case object dim_action_type
  extends DataSource {
  override def name: String = "dim_action_type"

  override def namespace: String = "coremodel"

  override def prefix: String = customer_prefix

  override def dimensionalized: DataFrame = this.read.as(this.name)
    .join(
      dim_funnel_stage.dimensionalized.as(dim_funnel_stage.name),
//      col(s"${dim_funnel_stage.name}.funnel_stage_id") === col(s"${this.name}.funnel_stage_id"),
      dim_funnel_stage("funnel_stage_id") === this("funnel_stage_id"),
      "left"
    )
}
