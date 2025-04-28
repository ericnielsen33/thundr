package com.thundr.measurement

import org.apache.spark.sql.DataFrame

case class SKUGroup(name: String, dataFrame: DataFrame)
