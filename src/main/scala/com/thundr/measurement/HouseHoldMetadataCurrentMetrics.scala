package com.thundr.measurement

import org.apache.spark.sql.{DataFrame, SaveMode}
import org.apache.spark.sql.functions._
import com.thundr.data._
import com.thundr.data.public_works.PublicWorksDataset

object HouseHoldMetadataCurrentMetrics
  extends BaseTable
    with PublicWorksDataset {

  override def prefix: String = customer_prefix

  override def name: String = "household_metadata_current_metrics"

  def partition_keys: Seq[String] = Seq.empty :+ "pipeline_id"

  override def create: DataFrame = {

    session.sql(
      s"""
         |CREATE OR REPLACE TABLE ${this.uri} (
         |                pipeline_id STRING NOT NULL,
         |                household_id STRING NOT NULL,
         |                own_brand_collection_id STRING NOT NULL,
         |                category_collection_id STRING NOT NULL,
         |                report_date DATE NOT NULL,
         |                individual_identity_keys ARRAY<STRING> NOT NULL,
         |                trip_cycle_avg_split_13w INT,
         |                trip_cycle_avg_split_14to26w INT,
         |                trip_cycle_avg_split_27to52w INT,
         |                category_cycle_avg_split_13w INT,
         |                category_cycle_avg_split_14to26w INT,
         |                category_cycle_avg_split_27to52w INT,
         |                own_brand_cycle_avg_split_13w INT,
         |                own_brand_cycle_avg_split_14to26w INT,
         |                own_brand_cycle_avg_split_27to52w INT,
         |                category_revenue_13w DOUBLE,
         |                category_revenue_14to26w DOUBLE,
         |                category_revenue_split_27to52w DOUBLE,
         |                own_brand_revenue_13w DOUBLE,
         |                own_brand_revenue_14to26w DOUBLE,
         |                own_brand_revenue_split_27to52w DOUBLE,
         |                CONSTRAINT product_collection_primary_key_constraint
         |                    PRIMARY KEY (pipeline_id, household_id)
         |            ) USING DELTA
         |""".stripMargin
    )

    this.read.limit(10)
  }

  override def append(df: DataFrame): DataFrame = {
    df.write
      .format("delta")
      .mode(SaveMode.Overwrite)
      .partitionBy(partition_keys: _*)
      .option("partitionOverwriteMode", "dynamic")
      .saveAsTable(uri)

    session.read.table(uri).limit(10)
  }

  def append(df: DataFrame, pipeline_id: String, own_brand_collection_id: String, category_collection_id: String): DataFrame = {
    append(
      df
      .withColumn("pipeline_id", lit(pipeline_id))
      .withColumn("own_brand_collection_id", lit(own_brand_collection_id))
      .withColumn("category_collection_id", lit(category_collection_id))
    )
  }
}
