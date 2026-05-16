package com.thundr.functions

import org.apache.spark.sql.Column
import org.apache.spark.sql.functions._

package object agg {

  def sum_when[T](predicate: T => Column, target: Column)(condition: T) = {
    sum(when(predicate(condition), target).otherwise(lit(0)))
  }

  def avg_when[T](predicate: T => Column, target: Column)(condition: T) = {
    avg(when(predicate(condition), target).otherwise(lit(null)))
  }

  def std_dev_when[T](predicate: T => Column, target: Column)(condition: T) = {
    stddev(when(predicate(condition), target).otherwise(lit(null)))
  }

  def count_distinct_when[T](predicate: T => Column, target: Column)(condition: T) = {
    count_distinct(when(predicate(condition), target).otherwise(lit(null)))
  }
}
