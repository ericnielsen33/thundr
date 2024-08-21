package com.thundr.data.coremodel

import com.thundr.data.DataSource

case object dim_brand
  extends  DataSource {

  override def name: String = "dim_brand"

  override def namespace: String = "coremodel"

  override def prefix: String = customer_prefix
}
