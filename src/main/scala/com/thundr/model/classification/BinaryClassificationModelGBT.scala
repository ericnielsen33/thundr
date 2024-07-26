package com.thundr.model.classification

import com.thundr.model.{FeaturesDataFrame, LabeledDataFrame}
import org.apache.spark.sql.{Column, DataFrame}
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.classification.{GBTClassificationModel, GBTClassifier}
import org.apache.spark.ml.feature.{IndexToString, StringIndexer, VectorIndexer}


case class BinaryClassificationModelGBT(labels: LabeledDataFrame, features: FeaturesDataFrame) {

}
