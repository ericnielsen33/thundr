package com.thundr.measurement


import org.apache.spark.sql.{DataFrame, SaveMode}
import org.apache.spark.sql.functions._
import io.delta.tables._
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
         |                hh_id STRING NOT NULL,
         |                collection_id STRING NOT NULL,
         |                sku_id STRING NOT NULL,
         |                order_date DATE NOT NULL,
         |                report_dimensions MAP<STRING, STRING>,
         |                purchased_product_total_cnt INT NOT NULL,
         |                purchased_product_total_usd DOUBLE NOT NULL
         |            ) USING DELTA
         |""".stripMargin
    )
    this.read.limit(10)
  }

}
