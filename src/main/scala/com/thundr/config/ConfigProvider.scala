package com.thundr.config

trait ConfigProvider {
  lazy val customer_prefix: String = scala.util.Properties.envOrNone("CUSTOMER_PREFIX").get.trim()
  lazy val audeince_xfer_root: String = scala.util.Properties.envOrNone("AUDIENCE_XFER_ROOT").get.trim()
  lazy val default_prefix: String = "p1pcat_prospect"
}