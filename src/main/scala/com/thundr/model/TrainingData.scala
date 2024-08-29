package com.thundr.model

import org.apache.spark.sql.DataFrame

case class TrainingData(features: Features, labels: Labels) {

  def formTrainingSubsets(training_path: String) = {

    val combined: DataFrame = features.withAlias
      .join(
        labels.withAlias,
        features.id.equalTo(labels.id),
        "inner"
      )

  }

}
