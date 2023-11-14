package com.thundr.data

import com.thundr.core.enums.{CustomerPrefix}
import org.apache.spark.sql.SparkSession

case class DataSource(
                       name: String,
                       namespace: String,
                       prefix: String,
                       spark: SparkSession
                     ) {
  def uri: String = prefix + "." + namespace + "." + name
  def read() = spark.read.table(uri)
}
