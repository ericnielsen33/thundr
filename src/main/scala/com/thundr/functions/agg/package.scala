package com.thundr.functions

import org.apache.spark.sql.Column
import org.apache.spark.sql.functions._

package object agg {

  def countWhen[T](column: Column, member: T): Column = sum(when(column.equalTo(member), 1).otherwise(0))

  def countDistinctWhen[T](distinct_column: Column, column: Column, member: T): Column = {
    countDistinct(when(column.equalTo(member), distinct_column).otherwise(null))
  }
}
