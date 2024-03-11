package com.thundr.data.experian

import com.thundr.data.DataSource
case object dim_experian_attribute
  extends DataSource {
  override def name: String = "dim_experian_attribute"

  override def namespace: String = "experian"

  override def prefix: String = default_prefix
}
