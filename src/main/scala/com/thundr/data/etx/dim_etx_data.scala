package com.thundr.data.etx

import com.thundr.data.DataSource
import org.apache.spark.sql.types._
case object dim_etx_data
  extends DataSource {
  override def name: String = "dim_etx_data"

  override def namespace: String = "etx"

  override def prefix: String = default_prefix
}