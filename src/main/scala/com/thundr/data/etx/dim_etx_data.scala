package com.thundr.data.etx

import com.thundr.data.{CoreID, DataSource}
import org.apache.spark.sql.types._
case object dim_etx_data
  extends DataSource
  with CoreID {
  override def name: String = "dim_etx_data"

  override def namespace: String = "etx"

  override def prefix: String = default_prefix
}