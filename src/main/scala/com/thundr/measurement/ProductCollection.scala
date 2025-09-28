package com.thundr.measurement

import java.time.LocalDate
import java.util.UUID
import org.apache.spark.sql.{DataFrame, SaveMode}
import org.apache.spark.sql.functions._
import com.thundr.data._
import com.thundr.core.SCD2

case object ProductCollection
  extends BaseTable
    with SCD2 with public_works.PublicWorksDataset {

  override def prefix: String = customer_prefix

  override def name: String = "scd2_products"

  override def lookup_key: String = "collection_id"

  override def merge_keys: Seq[String] = Seq.empty :+ "lookup_brand_id" :+ "sku_id"

  override def create: DataFrame = {
    session.sql(
      s"""
        |CREATE OR REPLACE TABLE ${this.uri} (
        |                collection_id STRING NOT NULL,
        |                brand_id INT NOT NULL,
        |                sku_id STRING NOT NULL,
        |                ${start_col_ref} DATE NOT NULL,
        |                ${end_col_ref} DATE,
        |                CONSTRAINT product_collection_primary_key_constraint
        |                    PRIMARY KEY (collection_id, brand_id, sku_id, start_date)
        |            ) USING DELTA
        |;
        |ALTER TABLE ${this.uri}
        |ADD CONSTRAINT ${this.name}_foreign_key_constraint
        |FOREIGN KEY(collection_id) REFERENCES ${ProductCollectionMeta.uri}
        |;
        |""".stripMargin
    )
    this.read.limit(10)
  }

  def insert_new_collenction(products: DataFrame): DataFrame = {
    val today: LocalDate = LocalDate.now()
    val collection_id: String = UUID.randomUUID().toString
    val insert_df = products
      .select(
        lit(collection_id).as("collection_id"),
        col("brand_id"),
        col("sku_id"),
        lit(today).as(start_col_ref),
        lit(null).as(end_col_ref)
      )

    insert_df.write
      .format("delta")
      .mode(SaveMode.Append)
      .saveAsTable(uri)

    current_entities(lit(collection_id))
  }
}
