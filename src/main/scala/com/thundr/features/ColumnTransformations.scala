package com.thundr.features

import org.apache.spark.sql.Column
import org.apache.spark.sql.expressions.{Window, WindowSpec}
import org.apache.spark.sql.functions._
object ColumnTransformations
 extends Serializable {
  def windowTimeAggregation(c: Column, time_col: Column, spec: WindowSpec) = ???

}
