package com.thundr.data

import com.thundr.config._
import org.apache.spark.sql.{DataFrame, SparkSession}

abstract class DataSource
  extends ConfigProvider {
  lazy val session: SparkSession = SparkSession.getActiveSession.get
  def namespace: String
  def name: String
  def prefix: String
  def uri: String = prefix + "." + namespace + "." + name
  def read: DataFrame = session.read.table(uri)
  override def toString: String = uri
}
