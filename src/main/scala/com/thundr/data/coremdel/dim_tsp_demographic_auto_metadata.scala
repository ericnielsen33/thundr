package com.thundr.data.coremdel

import com.thundr.data.DataSource
case object dim_tsp_demographic_auto_metadata
 extends DataSource {
  override def name: String = "dim_tsp_demographic_auto_metadata"

  override def namespace: String = "coremodel"

  override def prefix: String = default_prefix
}
