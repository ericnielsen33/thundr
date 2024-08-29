package com.thundr.core.services.audience_catalogue

import com.thundr.audience.Audience
import com.thundr.config.ConfigProvider
import io.delta.tables.DeltaTable
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

class AudienceCatalogueProvider(val session: SparkSession)
  extends Serializable
  with ConfigProvider {

  private val target_alias: String = "target"
  private val source_alias: String = "source"

  def uri: String = s"${customer_prefix}.public_works.fact_audience_member"
  def create(): DeltaTable = {
    val table: DeltaTable = {
      DeltaTable.create()
        .tableName(uri)
        .addColumn("audience",  dataType = "STRING", nullable = false)
        .addColumn("individual_identity_key",  dataType = "STRING", nullable = false)
        .addColumn("last_published",  dataType = "DATE", nullable = false)
        .addColumn("start_date",  dataType = "DATE", nullable = false)
        .addColumn("end_date",  dataType = "DATE", nullable = true)
        .partitionedBy("audience")
        .execute()
    }
    table
  }

  def insertNewAudience(name: String, dataFrame: DataFrame) = {
   val toInsert = dataFrame
      .select(
        col("individual_identity_key"),
        lit(name).as("audience"),
        current_date().as("last_published").cast(DateType),
        current_date().as("start_date").cast(DateType),
        lit(null).as("end_date").cast(DateType)
      )
      .dropDuplicates
      .toDF()

    toInsert
      .write
      .partitionBy("audience")
      .format("delta")
      .option("partitionOverwriteMode", "dynamic")
      .mode(SaveMode.Overwrite)
      .saveAsTable(uri)
  }

  def insertNewAudience(audience: Audience): Unit = insertNewAudience(audience.name, audience.seed)

  def deleteAudience(audience: Audience): Unit = {
    val table = DeltaTable.forPath(session, uri)
    table.delete(col("audience").equalTo(audience.name))
  }
//https://kontext.tech/article/1067/spark-dynamic-and-static-partition-overwrite
  def merge(audience_name: String, dataFrame: DataFrame): Unit = {
    val updates = dataFrame
    val prev: DataFrame = readAudience(audience_name).filter(col("end_date").isNotNull)
      .filter(col("last_published").lt(current_date()))
      .withColumn("last_published", current_date().cast(DateType))

    val curr: DataFrame = readAudience(audience_name).filter(col("end_date").isNull)
    val matched_records: DataFrame = curr.as("curr")
      .join(
        updates.as("updates"),
        col("curr.individual_identity_key").equalTo(col("updates.individual_identity_key")),
        "inner")
      .select(
        col("curr.audience").as("audience"),
        lit(current_date()).as("last_published").cast(DateType),
        col("curr.individual_identity_key").as("individual_identity_key"),
        col("curr.start_date").as("start_date").cast(DateType),
        lit(null).as("end_date").cast(DateType)
      )

    val unmatched_to_head: DataFrame = updates.as("updates")
      .join(
        curr.as("curr"),
        col("curr.individual_identity_key").equalTo(col("updates.individual_identity_key")),
        "leftanti")
      .select(
        lit(audience_name).as("audience"),
        lit(current_date()).as("last_published").cast(DateType),
        col("updates.individual_identity_key").as("individual_identity_key"),
        lit(current_date()).as("start_date").cast(DateType),
        lit(null).as("end_date").cast(DateType)
      )
    val unmatched_to_tail: DataFrame = curr.as("curr")
      .join(
        updates.as("updates"),
        col("curr.individual_identity_key").equalTo(col("updates.individual_identity_key")),
        "leftanti")
      .select(
        col("curr.audience").as("audience"),
        lit(current_date()).as("last_published").cast(DateType),
        col("curr.individual_identity_key").as("individual_identity_key"),
        col("curr.start_date").as("start_date"),
        lit(current_date()).as("end_date")
      )
    val scd2: DataFrame = prev
      .union(unmatched_to_tail)
      .union(matched_records)
      .union(unmatched_to_head)

    scd2.write
      .format("delta")
      .mode("overwrite")
      .partitionBy("audience")
      .option("partitionOverwriteMode", "dynamic")
      .saveAsTable(uri)
  }
  def read: DataFrame = session.read.table(uri)

  def readAudinece(audience: Audience): DataFrame = read.filter(col("audience").equalTo(audience.name))

  def readAudience(audience_name: String): DataFrame = read.filter(col("audience").equalTo(audience_name))
}
