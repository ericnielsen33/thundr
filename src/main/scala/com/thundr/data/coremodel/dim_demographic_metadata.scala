package com.thundr.data.coremodel

import com.thundr.data.DataSource

case object dim_demographic_metadata
  extends DataSource {
  override def name: String = "dim_demographic_metadata"
  override def namespace: String = "coremodel"
  override def prefix: String = default_prefix
}
