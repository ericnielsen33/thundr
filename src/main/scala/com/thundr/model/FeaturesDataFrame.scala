package com.thundr.model

import org.apache.spark.sql.{Column, DataFrame}
import org.apache.spark.sql.functions._

case class FeaturesDataFrame(labels: DataFrame, features_col_name: String = "features", id_col_name: String = "individual_identity_key")
