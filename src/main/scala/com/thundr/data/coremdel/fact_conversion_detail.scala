package com.thundr.data.coremdel

import com.thundr.data.DataSource

case object fact_conversion_detail
  extends DataSource {
  override def name: String = "fact_conversion_detail"
  override def namespace: String = "coremodel"
  override def prefix: String = customer_prefix
}
