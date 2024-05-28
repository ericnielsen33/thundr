package com.thundr.data.coremodel

import com.thundr.data.{CoreID, DataSource}

case object dim_tsp_demographic_data_auto
 extends DataSource
 with CoreID {
  override def name: String = "dim_tsp_demographic_data_auto"

  override def namespace: String = "coremodel"

  override def prefix: String = default_prefix
}
