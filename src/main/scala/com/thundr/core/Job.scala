package com.thundr.core

import org.apache.spark.sql.DataFrame
import scala.util.Try

trait Job[T] {

  def collectArgs(args: Array[String]): Try[T]

  def execute(args: T): Unit

  def main(args: Array[String]): Unit = {
    val arg_mapping = collectArgs(args)
    execute(arg_mapping.get)
  }
}
