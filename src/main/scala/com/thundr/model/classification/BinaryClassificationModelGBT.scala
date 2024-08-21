package com.thundr.model.classification

import com.thundr.model.{FeaturesDataFrame, LabeledDataFrame}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{Column, DataFrame}
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.classification.{GBTClassificationModel, GBTClassifier}
import org.apache.spark.ml.feature.{IndexToString, StringIndexer, VectorIndexer}


case class BinaryClassificationModelGBT(labels: LabeledDataFrame, features: FeaturesDataFrame) {

  def combine_features_labels: DataFrame = {
    features.features.as("features")
      .join(
        labels.labels.as("labels"),
        col(s"labels.${labels.id_col_name}").equalTo(col(s"features.${features.id_col_name}")),
        "inner")
      .withColumn(s"${labels.label_col_name}", coalesce(labels.label_col, lit(0)))

  }

  def split_test_train: Array[DataFrame] = ???

}
