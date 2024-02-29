package com.thundr.data.coremdel

import com.thundr.data.DataSource
case object dim_user_identity_type
  extends DataSource {
  override def name: String = "dim_user_identity_type"

  override def namespace: String = "coremodel"

  override def prefix: String = customer_prefix
}
