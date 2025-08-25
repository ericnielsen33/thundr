package com.thundr.model.classification

import org.apache.spark.sql.DataFrame

trait BinaryClassificationModel {

  def index_labels(unsampled_data: DataFrame): BinaryClassificationModel

  def train(training_data: DataFrame): BinaryClassificationModel = ???

  def test(test_dataset: DataFrame) = ???


//  phases of modeling
//  config -> validate params, and determine sample size for CV folds, estimate num classes
//   indexing, training, testing, scoring, reporting
}
