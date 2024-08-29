package com.thundr.model

import org.apache.spark.sql.{Column, DataFrame}
import org.apache.spark.sql.functions._

case class Labels(labels: DataFrame, label_col_name: String = "label", id_col_name: String = "individual_identity_key") {

  def label_col: Column = col(label_col_name)

  def id: Column = col(id_col_name)

  def read: DataFrame = labels

  def withAlias: DataFrame = read.as("labels")
}
