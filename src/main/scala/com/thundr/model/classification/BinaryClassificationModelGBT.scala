package com.thundr.model.classification

import com.thundr.model.{Features, Labels}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{Column, DataFrame}
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.classification.{GBTClassificationModel, GBTClassifier}
import org.apache.spark.ml.feature.{IndexToString, StringIndexer, VectorIndexer}


case class BinaryClassificationModelGBT(training_subset: DataFrame) {



}
