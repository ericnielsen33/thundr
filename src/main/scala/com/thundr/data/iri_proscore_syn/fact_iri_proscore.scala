package com.thundr.data.iri_proscore_syn

import com.thundr.data.DataSource
case object fact_iri_proscore
  extends DataSource {

  override def name: String = "fact_iri_proscore"

  override def namespace: String = "iri_proscore_syn"

  override def prefix: String = default_prefix

}