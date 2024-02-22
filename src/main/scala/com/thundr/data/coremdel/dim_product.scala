package com.thundr.data.coremdel

import com.thundr.data.DataSource

case object dim_product
  extends DataSource {
  override def name: String = "dim_product"

  override def namespace: String = "coremodel"

  override def prefix: String = customer_prefix
}
