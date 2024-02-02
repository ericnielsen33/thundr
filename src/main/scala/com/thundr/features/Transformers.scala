package com.thundr.features

import org.apache.spark.sql.{Column, DataFrame}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.DataType

object Transformers
  extends Serializable {
  def columnTransformer(df: DataFrame,
                        columns: Seq[Column],
                        transfomation: Column => Column,
                        naming_convention: String => String
                       ): DataFrame = {

    if (columns.isEmpty) df
    else {
      val curr = columns.head
      val transformed = df.withColumn(naming_convention(curr.toString()), transfomation(curr))
      columnTransformer(transformed, columns.tail, transfomation, naming_convention)
    }
  }
  def typedTransformer(df: DataFrame,
                       dType: DataType,
                       transformation: Column => Column,
                       naming_convention: String => String
                      ): DataFrame = {

    val columns: Seq[Column] = df.dtypes.toSeq
      .filter(elem => elem._2 == dType.toString)
      .map(elem => col(elem._1))

    columnTransformer(df, columns, transformation, naming_convention)
  }
}
