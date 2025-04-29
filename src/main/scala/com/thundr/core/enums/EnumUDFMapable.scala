package com.thundr.core.enums

import org.apache.spark.sql.functions.udf
import  org.apache.spark.sql.expressions.UserDefinedFunction

trait EnumUDFMapable[T] {

  def values: Seq[T]

  def isValid(ref: String): Boolean = this.values.exists(_.toString.equals(ref))

  def getValue(ref: String): Option[T] = this.values.find(_.toString.equals(ref))

  def isValidUDF: UserDefinedFunction = udf((ref: String) => isValid(ref))
}
