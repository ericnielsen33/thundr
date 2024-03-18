package com.thundr.data.data_axle

import com.thundr.data.DataSource
case object dim_data_axle_data
  extends DataSource {

  override def name: String = "dim_data_axle_data"

  override def namespace: String = "data_axle"

  override def prefix: String = default_prefix
}