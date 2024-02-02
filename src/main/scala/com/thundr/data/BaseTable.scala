package com.thundr.data

import io.delta.tables.DeltaTable
import org.apache.spark.sql.DataFrame

abstract class BaseTable
 extends DataSource {
  def create(): DeltaTable
  def overwrite(df: DataFrame): DeltaTable

  def append(df: DataFrame): DeltaTable


}
