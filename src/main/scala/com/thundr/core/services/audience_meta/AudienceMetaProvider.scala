package com.thundr.core.services.audience_meta

import com.thundr.config.ConfigProvider
import io.delta.tables.DeltaTable
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

class AudienceMetaProvider(val session: SparkSession)
  extends Serializable
  with ConfigProvider {

  def uri: String = s"${customer_prefix}.public_works.dim_audience"

  def create(): DeltaTable = {
    val table: DeltaTable = {
      DeltaTable.create()
        .tableName(uri)
        .addColumn(
          DeltaTable.columnBuilder("version_id")
            .dataType("STRING")
            .generatedAlwaysAs("sha(concat(name, inserted_at))")
            .build()
        )
        .addColumn("name", dataType = "STRING", nullable = false)
        .addColumn("inserted_at", dataType = "TIMESTAMP", nullable = false)
        .addColumn("refresh_cadence_days", dataType = "INT", nullable = true)
        .addColumn("collection", dataType = "STRING", nullable = true)
        .addColumn("ttl_days", dataType = "INT", nullable = false)
        .addColumn("premium_datasets", dataType = "STRING", nullable = true)
        .execute()
    }
    table
  }

  def read: DataFrame = session.read.table(uri)

  def append(meta: AudienceMetaSchema) = {
    import session.implicits._
    val df = Seq(meta).toDF()
    df.write.mode(SaveMode.Append).saveAsTable(uri)
  }
}
