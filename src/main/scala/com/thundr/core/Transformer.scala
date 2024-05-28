//package com.thundr.core
//
//import org.apache.spark.sql.DataFrame
//
//import scala.annotation.tailrec
//
//abstract class Transformer[T] {
//  def transformations[T]: Seq[T]
//  def transformer(item: T, input: DataFrame): DataFrame
//  def transform(input: DataFrame): DataFrame = {
//    @tailrec
//    def transformAll(input: DataFrame, iters: Seq[T]): DataFrame = {
//      if(iters.isEmpty) input
//      else { transformAll(transformer(iters.head, input), iters.tail) }
//    }
//    transformAll(input, transformations)
//  }
//}
