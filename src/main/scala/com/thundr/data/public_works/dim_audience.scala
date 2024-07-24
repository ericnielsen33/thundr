package com.thundr.data.public_works

import com.thundr.data.DataSource

case object dim_audience
  extends DataSource
  with PublicWorksDataset {

  override def name: String = "dim_audience"

  override def prefix: String = customer_prefix
}
