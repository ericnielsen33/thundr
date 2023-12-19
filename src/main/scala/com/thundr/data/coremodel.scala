package com.thundr.data


import com.thundr.config.ConfigProvider
import org.apache.spark.sql.SparkSession

object coremodel
  extends Serializable
  with ConfigProvider {
  val domain: String = "coremodel"
  def dim_user(implicit spark: SparkSession) = {
    val ds = DataSource("dim_user", domain, customer_prefix, spark)
    ds
  }
}
