package com.thundr.config

import org.apache.spark.sql.SparkSession
import com.thundr.util.FileConfBuilder.readFile

trait SessionProvider {
  lazy val configuration: scala.collection.mutable.Map[String, String] = readFile("conf.yaml")
  val sparkSession: SparkSession = SparkSession.builder()
    .master(configuration.getOrElse("master", "yarn"))
    .appName(configuration.getOrElse("appname", "analytics"))
    .getOrCreate()
}


