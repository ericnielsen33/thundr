package com.thundr.data.inmarket

import com.thundr.data.DataSource
case object dim_inmarket_segment
  extends DataSource {
  override def name: String = "dim_inmarket_segment"

  override def namespace: String = "inmarket_audience"

  override def prefix: String = default_prefix
}
