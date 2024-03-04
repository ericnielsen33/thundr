package com.thundr.data.coremodel

import com.thundr.data.DataSource
case object dim_dma_code
  extends DataSource {
  override def name: String = "dim_dma_code"

  override def namespace: String = "coremodel"

  override def prefix: String = default_prefix
}