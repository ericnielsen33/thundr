package com.thundr.data.public_works

import com.thundr.data.DataSource

case object fact_lifecyle_event
  extends DataSource
  with PublicWorksDataset {

  override def name: String = "fact_lifecyle_event"

  override def prefix: String = customer_prefix

}
