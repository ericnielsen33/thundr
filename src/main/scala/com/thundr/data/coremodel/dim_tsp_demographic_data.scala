package com.thundr.data.coremodel

import com.thundr.data.{CoreID, DataSource}

case object dim_tsp_demographic_data
  extends DataSource
    with TSPDataset
    with CoreID {
  def name: String = "dim_tsp_demographic_data"
  def prefix: String = default_prefix
}
