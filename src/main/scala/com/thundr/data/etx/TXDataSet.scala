package com.thundr.data.etx

import com.thundr.data.DataSet

trait TXDataSet {

  def namespace: String = "etx"
  def datasource_code: String = "E_TXSPEND"
}
