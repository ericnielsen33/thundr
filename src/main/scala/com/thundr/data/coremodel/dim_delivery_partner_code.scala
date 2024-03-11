package com.thundr.data.coremodel

import com.thundr.data.DataSource
import com.thundr.data.coremodel.fact_user_delivery_partner_avail.default_prefix
case object dim_delivery_partner_code
  extends DataSource {
  override def name: String = "dim_delivery_partner_code"

  override def namespace: String = "coremodel"

  override def prefix: String = default_prefix
}