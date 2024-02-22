package com.thundr.data.coremdel

import com.thundr.data.DataSource

case object fact_site_visit extends DataSource {
  override def name: String = "fact_site_visit"

  override def namespace: String = "coremodel"

  override def prefix: String = customer_prefix
}
