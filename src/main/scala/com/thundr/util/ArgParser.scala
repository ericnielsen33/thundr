package com.thundr.util

case class ArgParser(input: Array[String], val mapping: Map[String, String] = Map()) {
  def parse(args: Array[String]): ArgParser = {
    val argMapping = args
      .map {_.split("=")}
      .map {
        case Array(k, v) => k.toLowerCase().replace("--", "") -> v }
      .toMap
    ArgParser(args, argMapping)
  }
}
