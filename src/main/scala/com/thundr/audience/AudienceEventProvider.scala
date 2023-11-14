package com.thundr.audience

import io.delta.tables.DeltaTable
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

class AudienceEventProvider(session: SparkSession, prefix: String) {

  def uri: String = s"${prefix}.public_works.fact_audience_event"

  def append(event: AudienceEventSchema) = {
    import session.implicits._
    val df = Seq(event).toDF()
    df.write.mode(SaveMode.Append).saveAsTable(uri)
  }

  def read: DataFrame = session.read.table(uri)

  def create(): DeltaTable = {
    val table = DeltaTable.create()
      .tableName(uri)
      .addColumn(
        DeltaTable.columnBuilder("event_id")
          .dataType("STRING")
          .generatedAlwaysAs("sha(concat(name, timestamp))")
          .build()
      )
      .addColumn("name", dataType = "STRING", nullable = false)
      .addColumn("timestamp", dataType = "TIMESTAMP", nullable = false)
      .addColumn("action", dataType = "STRING", nullable = false)
      .addColumn("collection", dataType = "STRING", nullable = true)
      .execute()
    table
  }

}
