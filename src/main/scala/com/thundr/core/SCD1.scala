package com.thundr.core

import java.time.LocalDate
import org.apache.spark.sql.{Column, DataFrame}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
import com.thundr.data.BaseTable


trait SCD1 {

  this: BaseTable =>

  def lookup_key: String

  def lookup_col: Column = col(lookup_key)

  def parition_key_ref: String

  def date_col_ref: String = "end_date"

  def date_col: Column = col(date_col_ref)

  def date_col(alias: String): Column = col(s"${alias}.${date_col_ref}").cast(DateType)

  def date_col(expr: Column): Column = expr.as(date_col_ref).cast(DateType)

  def all_entities(entity: String): DataFrame = this.read.filter(lookup_col.equalTo(lit(entity)))

  def current_entities(entity: String): DataFrame = all_entities(entity).filter(date_col.isNull)

  def historical_entities(entity: String): DataFrame = all_entities(entity).filter(date_col.isNotNull)

  def historical_entities(entity: String,  date: LocalDate): DataFrame = all_entities(entity).filter(date_col.equalTo(date))

  overdef append(entity: String, updates: DataFrame) = {

    val archive_date = LocalDate.now()

    val current_to_historic: DataFrame = current_entities()


    val historic = historical_entities(entity)

    val current_entities = updates.withColumn(date_col_ref, lit(null).cast(DateType))

    val all_entities = current_to_historic
      .

  }


}
