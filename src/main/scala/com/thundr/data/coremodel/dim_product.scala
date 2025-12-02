package com.thundr.data.coremodel

import com.thundr.data.{DataSource, JSONColumn}

case object dim_product
  extends DataSource
  with JSONColumn {
  override def name: String = "dim_product"

  override def namespace: String = "coremodel"

  override def prefix: String = customer_prefix
}
