package com.thundr.data.coremodel

import com.thundr.data.{CoreID, DataSource}

case object dim_tsp_demographic_data
  extends DataSource
  with CoreID {
  override def name: String = "dim_tsp_demographic_data"
  override def namespace: String = "coremodel"
  override def prefix: String = default_prefix
}
