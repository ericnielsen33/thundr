package com.thundr.data.experian

import com.thundr.data.DataSet

trait ExperianDataSet {

  def namespace: String = "experian"
  def datasource_code: String = "3_EXPERIAN_CONSUMER"
}
