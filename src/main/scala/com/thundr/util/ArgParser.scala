package com.thundr.util

object ArgParser{
  def parse(args: Array[String]): Map[String, String] = {
    val argMapping = args
      .map {_.split("=")}
      .map {case Array(k, v) => k.trim.toLowerCase().replace("--", "") -> v.trim}
      .toMap
    argMapping
  }
}
