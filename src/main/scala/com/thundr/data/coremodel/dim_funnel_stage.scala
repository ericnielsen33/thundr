package com.thundr.data.coremodel

import com.thundr.data.DataSource
case object dim_funnel_stage
  extends DataSource {
  override def name: String = "dim_funnel_stage"

  override def namespace: String = "coremodel"

  override def prefix: String = customer_prefix
}