package com.thundr.data.etx

import com.thundr.data.{CoreID, DataSource}

case object dim_etx_data
  extends DataSource
    with TXDataSet
    with CoreID {

  def name: String = "dim_etx_data"
  def prefix: String = default_prefix
}