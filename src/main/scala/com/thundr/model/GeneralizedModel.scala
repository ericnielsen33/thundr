package com.thundr.model

import com.thundr.config.ConfigProvider
import org.apache.spark.ml.feature.{IndexToString, StringIndexer, VectorIndexer}

trait GeneralizedModel
  extends ConfigProvider {

  def base_directory = thundr_root

  def project_name: String

  def run_name: String

//  def label_indexer = {
//    val indexer = new StringIndexer()
//      .setInputCol("label")
//      .setOutputCol("indexedLabel")
//      .fit(feature_set)
//  }
}
