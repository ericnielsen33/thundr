package com.thundr.data.coremdel

import com.thundr.data.DataSource
import org.apache.spark.sql.DataFrame
case object dim_funnel_stage
  extends DataSource {
  override def name: String = "dim_funnel_stage"

  override def namespace: String = "coremodel"

  override def prefix: String = customer_prefix
}