package com.thundr.model

import com.thundr.config.ConfigProvider
import com.thundr.functions.vector.extract_value_from_vector
import org.apache.spark.ml.{Pipeline, PipelineModel, PipelineStage}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{Column, DataFrame}


trait GeneralizedModel
  extends ConfigProvider {

  def primary_key: Seq[Column]

  def base_directory = thundr_root

  def run_desc: String

  def run_id: String

  def output_path: String = base_directory + run_id

  def model_path: String = output_path + "/model"

  def pipeline_stages: Array[PipelineStage]

  def pipeline: Pipeline = new Pipeline().setStages(pipeline_stages)

  def model(trainning_df: DataFrame): PipelineModel = pipeline.fit(trainning_df)

  def model: PipelineModel = PipelineModel.load(model_path)

  def score(scoring_df: DataFrame): DataFrame = model.transform(scoring_df)
    .select({
      (Seq.empty :+
        lit(run_desc).as("run_desc") :+
        lit(run_id).as("run_id")) ++
        primary_key :+
        width_bucket(extract_value_from_vector(1)(col("probability")), lit(0) , lit(1), lit(20)).as("bin") :+
        extract_value_from_vector(0)(col("probability")).as("probabity_neg") :+
        extract_value_from_vector(1)(col("probability")).as("probabity_pos") :+
        col("predictedLabel").as("predicted_label")
    }: _*)


  def save_model(model: PipelineModel) = model.save(model_path)

}
