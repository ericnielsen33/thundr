package com.thundr.data.coremdel

import com.thundr.data.DataSource

case object xref_individual_to_child
  extends DataSource {
  override def name: String = "xref_individual_to_child"

  override def namespace: String = "coremodel"

  override def prefix: String = customer_prefix
}
