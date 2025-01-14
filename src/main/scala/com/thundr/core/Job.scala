package com.thundr.core
import scala.util.{Try, Success, Failure}
abstract class Job[T] {
  def main(args: Array[String]): Unit
  def collectArg(args: Array[String]): Option[T]
  def validateArgs(args: T): Option[Success[T]]
  def execute(args: T):  Option[Success[T]]
  def cleanup(args:T): Option[Success[T]]
  def end(args: T): Unit
}
