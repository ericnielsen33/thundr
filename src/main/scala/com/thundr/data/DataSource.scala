package com.thundr.data

import com.thundr.config._
import org.apache.spark.sql.{DataFrame, SparkSession, Column}
import org.apache.spark.sql.functions._

abstract class DataSource
  extends ConfigProvider {
  lazy val session: SparkSession = SparkSession.getActiveSession.get
  def namespace: String
  def name: String
  def prefix: String
  def uri: String = prefix + "." + namespace + "." + name
  def read: DataFrame = session.read.table(uri)
  def apply(colName: String): Column = col(s"${this.name}.${colName}")
  def dimensionalized: DataFrame = this.read
  override def toString: String = uri
}
