package com.thundr.core.services.audience_lifeycle

import com.thundr.config.{ConfigProvider, SessionProvider}
import io.delta.tables.DeltaTable
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}
import scala.collection.JavaConverters._
import scala.collection.mutable._

object AudienceLifecycleProvider
 extends Serializable
 with ConfigProvider
 with SessionProvider {
  def uri: String = s"${customer_prefix}.public_works.fact_lifecyle_event"
  def read: DataFrame = session.read.table(uri)
  def append(event: AudienceLifecycleSchema) = {
    import session.implicits._
    val df = Seq(event).toDF()
    df.write
      .format("delta")
      .mode(SaveMode.Append)
      .saveAsTable(uri)
  }
  def getHistory(audience_name: String): Seq[AudienceLifecycleSchema] = {
    import session.implicits._
    this.read.as[AudienceLifecycleSchema]
      .filter(col("name").equalTo(audience_name))
      .collectAsList()
      .asScala
      .sortWith(_.timestamp.toInstant.toEpochMilli < _.timestamp.toInstant.toEpochMilli)
  }
  def create(): DeltaTable = {
    val table: DeltaTable = {
      DeltaTable.create()
        .tableName(uri)
        .addColumn("name", dataType = "STRING", nullable = false)
        .addColumn("timestamp", dataType = "TIMESTAMP", nullable = false)
        .addColumn("event", dataType = "STRING", nullable = false)
        .addColumn("collection", dataType = "STRING", nullable = true)
        .addColumn("props_json", dataType = "STRING", nullable = true)
        .execute()
    }
    table
  }
}
