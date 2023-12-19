package com.thundr.core
import scala.util.{Try, Success, Failure}
abstract class Job {
  def main(args: Array[String]): Unit = ???
  def collectArgs(args: Array[String]): Map[String, String]

//  def validateArgs(args: Map[String, String], validator: Map[String, String => Boolean]): Try[Map[String, String]]

  def execute(args: Map[String, String])

}
