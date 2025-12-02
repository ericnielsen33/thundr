package com.thundr.measurement

import com.thundr.data.DataSource
import org.apache.spark.sql.DataFrame

trait ImpressionGroupQuery {

  def data_source: DataSource

  def execute: DataFrame

}
