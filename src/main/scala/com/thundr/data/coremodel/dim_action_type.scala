package com.thundr.data.coremodel

import com.thundr.data.DataSource
import org.apache.spark.sql.DataFrame

case object dim_action_type
  extends DataSource {
  override def name: String = "dim_action_type"

  override def namespace: String = "coremodel"

  override def prefix: String = customer_prefix

  override def dimensionalized: DataFrame = this.withAlias
    .join(
      dim_funnel_stage.dimensionalized,
      dim_funnel_stage("funnel_stage_id") === this("funnel_stage_id"),
      "left"
    )
}
