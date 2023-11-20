package com.thundr.audience

import io.delta.tables.DeltaTable
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}
import org.apache.spark.sql.functions._

class AudienceCatalogueProvider(val session: SparkSession, val prefix: String)
  extends Serializable {

  private val target_alias: String = "target"
  private val source_alias: String = "source"

  def uri: String = s"${prefix}.public_works.fact_audience_member"
  def create(): DeltaTable = {
    val table: DeltaTable = {
      DeltaTable.create()
        .tableName(uri)
        .addColumn("audience",  dataType = "STRING", nullable = false)
        .addColumn("individual_identity_key",  dataType = "STRING", nullable = false)
        .addColumn("last_published",  dataType = "DATE", nullable = false)
        .addColumn("start_date",  dataType = "DATE", nullable = false)
        .addColumn("end_date",  dataType = "DATE", nullable = true)
        .execute()
    }
    table
  }

  def insertNewAudience(audience: Audience): Unit = {
    audience.seed
      .withColumn("audience", lit(audience.name))
      .withColumnRenamed(audience.id, "individual_identity_key")
      .withColumn("last_published", current_date())
      .withColumn("start_date", current_date())
      .withColumn("end_date", lit(null))
      .write
      .format("delta")
      .mode(SaveMode.Append)
      .saveAsTable(uri)
  }
//  need to explore whenNotMatedBy source to set non-null end_Date to represent audience membership removal
  def merge(audience: Audience): Unit = {
    val target = DeltaTable.forPath(session, uri)
    val source = audience.seed
      .withColumn("audience", lit(audience.name))
      .withColumn("run_date", current_date())
      .withColumn(colName = "null_end_date", lit(null))

    val merged = target.as("target")
      .merge(
        source = source.as("updates"),
        condition = s"${target_alias}.individual_identity_key = ${source_alias}.${audience.id} AND ${target_alias}.audience = ${source_alias}.audience"
      )
      .whenMatched
      .updateExpr(
        Map(
          "audience" -> s"${target_alias}.audience",
          "individual_identity_key" -> s"${target_alias}.individual_identity_key",
          "last_published" -> s"${source_alias}.run_date",
          "start_date" -> s"${target_alias}.start_date",
          "end_date" -> s"${source_alias}.null_end_date"
        ))
      .whenNotMatched
      .insertExpr(
        Map(
          "audience" -> s"${source_alias}.audience",
          "individual_identity_key" -> s"${source_alias}.individual_identity_key",
          "last_published" -> s"${source_alias}.run_date",
          "start_date" -> s"${source_alias}.run_date",
          "end_date" -> s"${source_alias}.null_end_date"
        ))
    merged.execute()
  }

  def read: DataFrame = {
    session.read.table(uri)
  }

  def readAudinece(audience: Audience): DataFrame = {
    session.read.table(uri).filter(col("audience").equalTo(lit(audience.name)))
  }
}
