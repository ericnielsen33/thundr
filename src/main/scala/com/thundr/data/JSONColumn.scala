package com.thundr.data


import org.apache.spark.sql.Column
import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._
import com.thundr.functions.json.extract_json_typed

trait JSONColumn {

  this: DataSource =>

  def json_column_ref: String = "custom_attr_json"

  def json_column_root: String = "$.descriptors"

  def extract_json_field(json_field_ref: String, datatype: DataType = StringType): Column = {

    val col_ref_with_alias = this.name + "." + this.json_column_ref

    val extract = extract_json_typed(col(col_ref_with_alias), json_column_root)

    extract(json_field_ref, datatype)
  }
}
