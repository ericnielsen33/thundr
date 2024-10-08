package com.thundr.model

import org.apache.spark.sql.{Column, DataFrame}
import org.apache.spark.sql.functions._

case class Features(features: DataFrame, features_col_name: String = "features", id_col_name: String = "individual_identity_key") {

  def features_col: Column = col(features_col_name)

  def id: Column = col(id_col_name)

  def read: DataFrame = features

  def withAlias: DataFrame = features.as("features")

}
