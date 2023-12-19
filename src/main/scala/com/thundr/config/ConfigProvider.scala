package com.thundr.config

trait ConfigProvider {
  lazy val customer_prefix: String = scala.util.Properties.envOrNone("CUSTOMER_PREFIX").get
}
