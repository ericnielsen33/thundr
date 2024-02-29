package com.thundr.data.coremdel

import com.thundr.data.DataSource

case object dim_action_type
  extends DataSource {
  override def name: String = "dim_action_type"

  override def namespace: String = "coremodel"

  override def prefix: String = customer_prefix
}
