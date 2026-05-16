package com.thundr.measurement

import org.apache.spark.sql.{DataFrame, SaveMode}
import org.apache.spark.sql.functions._
import io.delta.tables._
import com.thundr.data._
import com.thundr.data.public_works.PublicWorksDataset


case object ImpressionGroup
  extends BaseTable with PublicWorksDataset {

  def partition_keys: Seq[String] = Seq.empty :+ "group_id" :+ "impression_date"

  override def prefix: String = customer_prefix

  override def name: String = "impressions"

  override def create: DataFrame = {
    session.sql(
      s"""
         |CREATE OR REPLACE TABLE ${this.uri} (
         |                group_id STRING NOT NULL,
         |                impression_date DATE NOT NULL,
         |                user_identity_key STRING NOT NULL,e
         |                user_identity_type_id INT NOT NULL,
         |                report_dimensions MAP<STRING, STRING>,
         |                impression_count INT NOT NULL,
         |                click_count INT,
         |                media_cost DOUBLE
         |            ) USING DELTA
         |""".stripMargin
    )
    this.read.limit(10)
  }

  override def append(df: DataFrame): DataFrame = {
    df
      .select(
        col("group_id"),
        col("impression_date"),
        col("user_identity_key"),
        col("user_identity_type_id"),
        col("report_dimensions"),
        col("impression_count"),
        col("click_count"),
        col("media_cost"))
      .write
      .format("delta")
      .mode(SaveMode.Overwrite)
      .partitionBy(partition_keys: _*)
      .option("partitionOverwriteMode", "dynamic")
      .saveAsTable(uri)

    session.read.table(uri).limit(10)
  }

  def append(df: DataFrame, group_id: String): DataFrame = append(df.withColumn("group_id", lit(group_id)))

  def delete_group(group_id: String) = {
    val deltaTable = DeltaTable.forName(session, this.uri)
    deltaTable.delete(col("group_id") === group_id)
  }

}
