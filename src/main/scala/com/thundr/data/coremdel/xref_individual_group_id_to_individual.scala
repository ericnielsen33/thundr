package com.thundr.data.coremdel

import com.thundr.data.DataSource

case object xref_individual_group_id_to_individual
  extends DataSource {
  override def name: String = "xref_individual_group_id_to_individual"

  override def namespace: String = "coremodel"

  override def prefix: String = customer_prefix
}
