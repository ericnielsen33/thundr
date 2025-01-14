package com.thundr.core.enums


import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.functions.udf

trait EnumUDF {

  this: EnumUDFMapable =>

  def matchEnum: String => UserDefinedFunction = ???

}
