package com.thundr.data.coremodel

import com.thundr.data.DataSource
case object fact_user_attribute
  extends DataSource {
  override def name: String = "fact_user_attribute"

  override def namespace: String = "coremodel"

  override def prefix: String = customer_prefix
}