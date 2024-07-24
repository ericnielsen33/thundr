package com.thundr.data.public_works

import com.thundr.data.{CoreID, DataSource, HouseHoldID}

case object fact_audience_member
  extends DataSource
  with CoreID
  with HouseHoldID
  with PublicWorksDataset {

  override def name: String = "fact_audience_member"

  override def prefix: String = customer_prefix


}
