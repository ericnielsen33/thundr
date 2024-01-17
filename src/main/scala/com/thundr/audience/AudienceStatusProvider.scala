package com.thundr.audience

import com.thundr.config.ConfigProvider
import scala.collection.mutable._
import collection.JavaConverters._
import io.delta.tables.DeltaTable
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}
import org.apache.spark.sql.functions._

import java.sql.Timestamp

case class AudienceStatusSchema(name: String, timestamp: Timestamp, status: String, dac_id: String, rqs_id: String)
case class DacPollResponse(name: String, status: String, dac_id: String, rqs_id: String)
class AudienceStatusProvider(val session: SparkSession)
  extends Serializable
  with ConfigProvider {

  def uri: String = s"${customer_prefix}.public_works.fact_dac_poll_status"

  def read: DataFrame = session.read.table(uri)

  def getHistory(audience: Audience): Seq[AudienceStatusSchema] = {
    import session.implicits._
    this.read.as[AudienceStatusSchema]
      .filter(col("name").equalTo(audience.name))
      .collectAsList()
      .asScala
      .sortWith(_.timestamp.toString < _.timestamp.toString)
  }

  def getCurrentStatus(audience: Audience) = {
    getHistory(audience).reverse.head
  }

  def appendPoll(poll: DacPollResponse) = {
    import session.implicits._
    val status = AudienceStatusSchema(
      name = poll.name,
      timestamp = new Timestamp(System.currentTimeMillis()),
      status = poll.status,
      dac_id = poll.dac_id,
      rqs_id = poll.rqs_id
    )

    val df = session.sparkContext.parallelize(List(status)).toDF()

    df.write
      .format("delta")
      .mode(SaveMode.Append)
      .saveAsTable(uri)
  }

  def create(): DeltaTable = {
    val table: DeltaTable = {
      DeltaTable.create()
        .tableName(uri)
        .addColumn("name", dataType = "STRING", nullable = false)
        .addColumn("timestamp", dataType = "TIMESTAMP", nullable = false)
        .addColumn("status", dataType = "STRING", nullable = false)
        .addColumn("dac_id", dataType = "STRING", nullable = false)
        .addColumn("rqs_id", dataType = "STRING", nullable = true)
        .execute()
    }
    table
  }
}
