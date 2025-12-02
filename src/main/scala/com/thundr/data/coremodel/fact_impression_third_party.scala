package com.thundr.data.coremodel

import com.thundr.data.{DataSource, UserIdentityKey}

case object fact_impression_third_party
  extends DataSource
  with UserIdentityKey {

  override def name: String = "fact_impression_third_party_feed"
  override def namespace: String = "coremodel"
  override def prefix: String = customer_prefix

}
