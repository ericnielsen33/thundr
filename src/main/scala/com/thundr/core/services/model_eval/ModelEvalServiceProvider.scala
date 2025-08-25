package com.thundr.core.services.model_eval

import java.sql.Timestamp
import io.delta.tables.DeltaTable
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
import com.thundr.core.services.CoreDataService

//object ModelEvalServiceProvider
//  extends CoreDataService[ModelEvalSchema] {
//
//  override def table_name: String = "dim_model_eval"
//
//  override def create: DeltaTable = {
//    val table: DeltaTable = {
//      DeltaTable.create()
//        .tableName(uri)
//        .addColumn("brand_id", dataType = "INT", nullable = false)
//        .addColumn("project_name", dataType = "STRING", nullable = false)
//        .addColumn("model_name", dataType = "STRING", nullable = false)
//        .addColumn("run_id", dataType = "STRING", nullable = false)
//        .addColumn("trigger_time", dataType = "TIMESTAMP", nullable = false)
//        .addColumn("start_time", dataType = "TIMESTAMP", nullable = true)
//        .addColumn("end_time", dataType = "TIMESTAMP", nullable = true)
//        .addColumn("model_path", dataType = "STRING", nullable = true)
//        .addColumn("metrics",
//          ArrayType(StructType(
//            Seq(
//              StructField("metric_name", StringType),
//              StructField("training_metric_val", DoubleType, nullable = true),
//              StructField("test_metric_val", DoubleType, nullable = true)
//            )
//          )))
//        .addColumn("params",
//          ArrayType(StructType(
//            Seq(
//              StructField("key", StringType),
//              StructField("value", StringType),
//            )
//          )), nullable = true)
//        .execute()
//    }
//    table
//  }
//
//  override def read: S = ???
//
//  override def overwrite: CoreDataService[ModelEvalSchema] = ???
//
//  override def append: CoreDataService[ModelEvalSchema] = ???
//
//  override def delete: CoreDataService[ModelEvalSchema] = ???
//
//}
