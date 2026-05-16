package com.thundr.measurement


import org.apache.spark.sql.{DataFrame, SaveMode}
import org.apache.spark.sql.functions._
import io.delta.tables._
import com.thundr.data._
import com.thundr.data.public_works.PublicWorksDataset

case object ProductCollectionAggDaily
 extends BaseTable with PublicWorksDataset {

  override def prefix: String = default_prefix

  override def name: String = "conversions_agg_daily"

  override def create: DataFrame = {
    session.sql(
      s"""
         |CREATE OR REPLACE TABLE ${this.uri} (
         |                id STRING NOT NULL,
         |                id_type STRING NOT NULL,
         |                collection_id STRING NOT NULL,
         |                sku_id STRING NOT NULL,
         |                order_date DATE NOT NULL,
         |                purchased_product_total_usd DOUBLE NOT NULL
         |                purchased_product_total_cnt INT NOT NULL
         |                CONSTRAINT conversions_agg_daily_primary_key_constraint
         |                    PRIMARY KEY (id, id_type, collection_id, sku_id, order_date)
         |            ) USING DELTA
         |;
         |""".stripMargin
    )
    this.read.limit(10)
  }

}
