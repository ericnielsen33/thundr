package com.thundr.data.coremdel

import com.thundr.data.DataSource

case object dim_tsp_demographic_data_auto
 extends DataSource {
  override def name: String = "dim_tsp_demographic_data_auto"

  override def namespace: String = "coremodel"

  override def prefix: String = default_prefix
}
