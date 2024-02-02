package com.thundr.data.coremdel

import com.thundr.data.DataSource

object dim_tsp_demographic_data
  extends DataSource {
  override def name: String = "dim_tsp_demographic_data"
  override def namespace: String = "coremodel"
  override def prefix: String = default_prefix
}
