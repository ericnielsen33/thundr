package com.thundr.data.coremdel

import com.thundr.data.DataSource

object dim_demographic_metadata
  extends DataSource {
  override def name: String = "dim_demographic_metadata"
  override def namespace: String = "coremodel"
  override def prefix: String = default_prefix
}
