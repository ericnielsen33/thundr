package com.thundr.data.iri_proscore_syn

import com.thundr.data.DataSource
case object dim_iri_gen_merch
  extends DataSource {

  override def name: String = "dim_iri_gen_merch"

  override def namespace: String = "iri_proscore_syn"

  override def prefix: String = default_prefix

}