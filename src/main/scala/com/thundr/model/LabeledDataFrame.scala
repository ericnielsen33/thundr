package com.thundr.model

import org.apache.spark.sql.{Column, DataFrame}
import org.apache.spark.sql.functions._

case class LabeledDataFrame(labels: DataFrame, label_col_name: String = "label", id_col_name: String = "individual_identity_key") {

  def label_col: Column = col(label_col_name)
}
