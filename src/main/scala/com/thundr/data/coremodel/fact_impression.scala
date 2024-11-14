package com.thundr.data.coremodel

import com.thundr.data.{CoreID, DataSource}
import org.apache.spark.sql.DataFrame

case object fact_impression
  extends DataSource
    with CoreID {
  override def name: String = "fact_impression"
  override def namespace: String = "coremodel"
  override def prefix: String = customer_prefix

}