package com.thundr.data.data_axle

import com.thundr.data.DataSource
case object dim_data_axle_consumer_metadata
  extends DataSource {

  override def name: String = "dim_data_axle_consumer_metadata"

  override def namespace: String = "data_axle"

  override def prefix: String = default_prefix
}