package com.thundr.core

import org.apache.spark.sql.{Column, DataFrame}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
import com.thundr.data.BaseTable
import io.delta.tables.DeltaTable


trait SCD2 {

  this: BaseTable =>

  def lookup_key: String

  def lookup_col: Column = col(lookup_key)

  def parition_key_ref: String

  def partition_col: Column = col(parition_key_ref)

  def merge_keys: Seq[String]

  def merge_columns: Seq[Column] = merge_keys.map{ elem => col(elem).as(elem)}

  def start_col_ref: String = "start_date"

  def end_col_ref: String = "end_date"

  def start_col: Column = col(start_col_ref).cast(DateType)

  def start_col(alias: String): Column = col(s"${alias}.${start_col_ref}").cast(DateType)

  def start_col(expr: Column): Column = expr.as(start_col_ref).cast(DateType)

  def end_col: Column = col(end_col_ref).cast(DateType)

  def end_col(alias: String): Column = col(s"${alias}.${end_col_ref}").cast(DateType)

  def end_col(expr: Column): Column = expr.as(end_col_ref).cast(DateType)

  def historical_entities: DataFrame = this.read.filter(end_col.isNotNull)

  def historicial_entities(lookup_val: Column): DataFrame = {
    this.historical_entities.filter(lookup_col.equalTo(lookup_val))
  }

  def current_entities: DataFrame = this.read.filter(end_col.isNull)

  def current_entities(lookup_val: Column): DataFrame = {
    this.current_entities.filter(lookup_col.equalTo(lookup_val))
  }

  def matched_current_entities(updates: DataFrame, lookup_val: Column): DataFrame = {
    current_entities(lookup_val).as("current_entities")
      .join(
        updates.as("updates"),
        merge_keys
          .map{ key => col(s"updates.${key}").equalTo(col(s"current_entities.${key}"))}
          .reduceLeft{ _ || _ },
        "inner")
      .select({
          (Seq.empty :+ lookup_col) ++
            merge_columns :+
            start_col("current_entities") :+
            end_col(lit(null))
        }: _*)
  }

  def unmatched_current_entities(updates: DataFrame, lookup_val: Column): DataFrame = {
    updates.as("updates")
      .join(
        current_entities(lookup_val).as("current_entities"),
        merge_keys
          .map{ key => col(s"updates.${key}").equalTo(col(s"current_entities.${key}"))}
          .reduceLeft{ _ || _ },
        "leftanti")
      .select({
          (Seq.empty :+ lookup_col) ++
            merge_columns :+
            start_col(current_date()) :+
            end_col(lit(null))
        }: _*)
  }

  def unmatched_historical_entities(updates: DataFrame, lookup_val: Column): DataFrame = {
    current_entities(lookup_val).as("current_entities")
      .join(
        updates.as("updates"),
        merge_keys
          .map{ key => col(s"updates.${key}").equalTo(col(s"current_entities.${key}"))}
          .reduceLeft{ _ || _ },
        "leftanti")
      .select({
          (Seq.empty :+ lookup_col) ++
            merge_columns :+
            start_col("current_entities") :+
            end_col(current_date())
        }: _*)
  }

  def merge(updates: DataFrame, lookup_val: Column) = {
    val merged = matched_current_entities(updates,lookup_val)
      .union(unmatched_current_entities(updates, lookup_val))
      .union(unmatched_historical_entities(updates, lookup_val))
      .union(historicial_entities(lookup_val))

    merged.write
      .format("delta")
      .mode("overwrite")
      .partitionBy(parition_key_ref)
      .option("partitionOverwriteMode", "dynamic")
      .saveAsTable(uri)
  }
}
