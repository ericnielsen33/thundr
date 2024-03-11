package com.thundr.config

import org.apache.spark.sql.SparkSession

trait SessionProvider {
  lazy val session: SparkSession = SparkSession.getActiveSession.get
}


