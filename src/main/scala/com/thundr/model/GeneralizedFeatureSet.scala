package com.thundr.model

import com.thundr.data._
import com.thundr.data.public_works.PublicWorksDataset
import org.apache.spark.sql.{Column, DataFrame}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
import org.apache.spark.sql.SaveMode._

trait GeneralizedFeatureSet
  extends BaseTable with PublicWorksDataset {

  def features_col_ref: String = "features"

  def label_col_ref: String = "label"

  def features_col: Column = col(features_col_ref)

  def label_col: Column = col(label_col_ref).cast(DoubleType)

  def run_id: String

  def primary_key: Seq[String]

  def feature_type: String

  override def overwrite(df: DataFrame): DataFrame = {
    df.write
      .mode(Overwrite)
      .option("partitionOverwriteMode", "dynamic")
      .format("delta")
      .partitionBy("run_id")
      .saveAsTable(uri)

    val sample = this.read.filter(col("run_id").equalTo(lit(run_id)))

    sample
  }

}
