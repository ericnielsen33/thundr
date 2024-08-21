package com.thundr.functions

import org.apache.spark.sql.Column
import  org.apache.spark.sql.functions._
import org.apache.spark.sql.types._

package object json {
  def empty_str_to_null(input: Column): Column = when(input.equalTo(""), null).otherwise(input)

  def extract_json_typed(
                          column: Column,
                          json_field_ref: String,
                          datatype: DataType,
                          json_root: String = "$.descriptors"): Column = {
    empty_str_to_null(trim(get_json_object(column, json_root + s"${json_field_ref}"))).cast(datatype)
  }
}
