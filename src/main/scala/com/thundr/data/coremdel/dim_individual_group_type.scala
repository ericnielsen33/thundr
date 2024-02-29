package com.thundr.data.coremdel

import com.thundr.data.DataSource
case object dim_individual_group_type
  extends DataSource {
  override def name: String = "dim_individual_group_type"

  override def namespace: String = "coremodel"

  override def prefix: String = default_prefix
}
