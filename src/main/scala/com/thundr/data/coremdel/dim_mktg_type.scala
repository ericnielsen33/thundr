package com.thundr.data.coremdel

import com.thundr.data.DataSource
case object dim_mktg_type
  extends DataSource {
  override def name: String = "dim_mktg_type"

  override def namespace: String = "coremodel"

  override def prefix: String = default_prefix
}