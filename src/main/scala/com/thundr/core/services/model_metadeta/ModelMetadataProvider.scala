package com.thundr.core.services.model_metadeta

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.SaveMode
import io.delta.tables.DeltaTable
import com.thundr.config.{ConfigProvider, SessionProvider}

object ModelMetadataProvider
  extends SessionProvider
  with ConfigProvider {

  def uri: String = s"${customer_prefix}.public_works.dim_model_metadata"

  def read: DataFrame = session.read.table(uri)

  def append(model_meta: ModelMetadataSchema): Unit = {
    import session.implicits._
    val df = Seq(model_meta).toDF()
    df.write
      .format("delta")
      .mode(SaveMode.Append)
      .saveAsTable(uri)
  }

  def create(): DeltaTable = {
    val table: DeltaTable = {
      DeltaTable.create()
        .tableName(uri)
        .addColumn("project_name", dataType = "STRING", nullable = false)
        .addColumn("model_name", dataType = "STRING", nullable = false)
        .addColumn("run_id", dataType = "STRING", nullable = false)
        .addColumn("run_date", dataType = "DATE", nullable = false)
        .addColumn("brand_id", dataType = "STRING", nullable = false)
        .addColumn("metric_name", dataType = "STRING", nullable = false)
        .addColumn("training_metric_val", dataType = "DOUBLE", nullable = false)
        .addColumn("test_metric_val", dataType = "DOUBLE", nullable = false)
        .addColumn("model_path", dataType = "STRING", nullable = false)
        .execute()
    }
    table
  }


}
