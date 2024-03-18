package com.thundr.data.coremodel

import com.thundr.data.DataSource

case object dim_order_channel
  extends DataSource {
  override def name: String = "dim_order_channel"

  override def namespace: String = "coremodel"

  override def prefix: String = default_prefix
}
