package com.thundr.data

import org.apache.spark.sql.{DataFrame, SaveMode}

abstract class BaseTable
 extends DataSource {

  def create: DataFrame

  def overwrite(df: DataFrame): DataFrame = {

    df.write
      .format("delta")
      .mode(SaveMode.Overwrite)
      .saveAsTable(uri)

    session.read.table(uri).limit(10)
  }

  def append(df: DataFrame): DataFrame = {

    df.write
      .format("delta")
      .mode(SaveMode.Append)
      .saveAsTable(uri)

    session.read.table(uri).limit(10)
  }
}
