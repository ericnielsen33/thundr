package com.thundr.core
import scala.util.{Try, Success, Failure}
abstract class Job[T] {
  def main(args: Array[String]): Unit
  def collectArg(args: Array[String]): T
  def execute(args: T): Unit
  def end(args: T): Unit
}
