package com.thundr.data.coremodel

import com.thundr.data.DataSource

case object dim_supply_type
  extends DataSource {
  override def name: String = "dim_supply_type"

  override def namespace: String = "coremodel"

  override def prefix: String = default_prefix
}