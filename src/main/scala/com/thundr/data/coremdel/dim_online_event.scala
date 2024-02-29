package com.thundr.data.coremdel

import com.thundr.data.DataSource

case object dim_online_event
  extends DataSource {
  override def name: String = "dim_online_event"

  override def namespace: String = "coremodel"

  override def prefix: String = customer_prefix
}
