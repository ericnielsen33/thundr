package com.thundr.data.experian

import com.thundr.data.DataSource
case object dim_experian_attribute
  extends DataSource
    with ExperianDataSet {

  override def name: String = "dim_experian_attribute"

  override def prefix: String = default_prefix
}
