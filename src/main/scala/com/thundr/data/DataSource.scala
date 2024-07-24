package com.thundr.data

import com.thundr.audience.Audience
import com.thundr.config._
import org.apache.spark.sql.{Column, DataFrame, SparkSession}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.StructType


abstract class DataSource
  extends ConfigProvider with SessionProvider {
  def namespace: String
  def name: String
  def prefix: String
  def uri: String = prefix + "." + namespace + "." + name
  def read: DataFrame = session.read.table(uri)
  def apply(colName: String): Column = col(s"$name.$colName")
  def withAlias: DataFrame = this.read.as(this.name)
  def dimensionalized: DataFrame = withAlias
  def schema: Option[StructType] = None
  def toAudience: Audience = Audience(this.read.select("individual_identity_key").distinct(), this.toString)
  override def toString: String = uri
}
