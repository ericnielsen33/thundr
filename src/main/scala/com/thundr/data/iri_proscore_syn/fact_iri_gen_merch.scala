package com.thundr.data.iri_proscore_syn

import com.thundr.data.DataSource
case object fact_iri_gen_merch
  extends DataSource {

  override def name: String = "fact_iri_gen_merch"

  override def namespace: String = "samba"

  override def prefix: String = default_prefix

}