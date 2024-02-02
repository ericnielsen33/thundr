package com.thundr.config

trait ConfigProvider {
  lazy val customer_prefix: String = scala.util.Properties.envOrNone("CUSTOMER_PREFIX").get
  lazy val default_prefix: String = "p1pcat_prospect"
}
//p1gm_prospect