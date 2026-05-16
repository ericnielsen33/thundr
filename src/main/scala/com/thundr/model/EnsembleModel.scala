package com.thundr.model

import org.apache.spark.ml.PipelineModel

trait EnsembleModel
  extends GeneralizedModel {

  def constituent_models: Array[PipelineModel]

}
