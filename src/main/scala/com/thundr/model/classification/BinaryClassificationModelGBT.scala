package com.thundr.model.classification


import scala.util.Try
import java.time.{LocalDate, LocalDateTime}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
import org.apache.spark.sql.{Column, DataFrame}
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.classification.{GBTClassificationModel, GBTClassifier}
import org.apache.spark.ml.feature.{IndexToString, StringIndexer}
import org.apache.spark.ml.evaluation.BinaryClassificationEvaluator
import org.apache.spark.ml.tuning.{CrossValidator, ParamGridBuilder}
import com.thundr.config.{ConfigProvider, SessionProvider}
import com.thundr.core.Job
import com.thundr.util.ArgParser

object BinaryClassificationModelGBT
  extends Job[BinaryClassifcationModelParmset]
  with SessionProvider
  with ConfigProvider {


  private val labels_table_ref: String = s"$customer_prefix.public_works.dim_model_seed"
  private val labels_table: DataFrame = session.table(labels_table_ref)

  private val num_classes: Int = 2
  private val positive_label: Int = 1
  private val negative_label: Int = 0
  private val label_col_ref: String = "label"
  private val indexed_label_col_ref: String = "indexed_label"

  private val label_indexer: StringIndexer = new StringIndexer()
    .setInputCol(label_col_ref)
    .setOutputCol(indexed_label_col_ref)
    .setStringOrderType("alphabetAsc")

  private val training_schema: Seq[Column] = (Seq.empty :+
    "individual_identity_key" :+
    "features" :+
    "label").map(col)

  private def output_path(parmset: BinaryClassifcationModelParmset) = {
    analytics_root + parmset.project_name + "/" + parmset.model_name + "/" + parmset.run_id
  }

  private def model_path(parmset: BinaryClassifcationModelParmset) = {
    output_path((parmset)) + "/model"
  }

  private def labels(parmset: BinaryClassifcationModelParmset): DataFrame = {
    labels_table.filter(col("seed_ref").equalTo(parmset.seed_ref))
  }

  override def collectArgs(args: Array[String]): Try[BinaryClassifcationModelParmset] = Try {
    val parsed = ArgParser.parse(args)
    val parmset = BinaryClassifcationModelParmset(
      parsed("brand_id").toInt,
      parsed("project_name"),
      parsed("model_name"),
      parsed("feature_table_ref"),
      parsed("seed_ref"),
      parsed("run_id"),
      parsed("run_dt")
    )
    parmset
  }

  def train(parmset: BinaryClassifcationModelParmset) = ???

  override def execute(args: BinaryClassifcationModelParmset): Unit = ???

}
