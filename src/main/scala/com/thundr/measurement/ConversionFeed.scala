package com.thundr.measurement


import org.apache.spark.sql.{DataFrame, SaveMode}
import org.apache.spark.sql.functions._
import com.thundr.data._
import com.thundr.data.public_works.PublicWorksDataset


case object ConversionFeed
  extends BaseTable with PublicWorksDataset {


  override def prefix: String = customer_prefix

  override def name: String = "conversions"

  override def create: DataFrame = {
    session.sql(
      s"""
         |CREATE OR REPLACE TABLE ${this.uri} (
         |                feed_id STRING NOT NULL,
         |                collection_id STRING NOT NULL,
         |                individual_identity_key STRING NOT NULL,
         |                household_id STRING,
         |                report_dimensions MAP<STRING, STRING>,
         |                purchase_date DATE NOT NULL,
         |                unit_count INT NOT NULL,
         |                revenue DOUBLE NOT NULL
         |            ) USING DELTA
         |""".stripMargin
    )
    this.read.limit(10)
  }

}
