package com.thundr.config

trait ConfigProvider {
  lazy val customer_prefix: String = scala.util.Properties.envOrNone("CUSTOMER_PREFIX").get.trim()
  lazy val audeince_xfer_root: String = scala.util.Properties.envOrNone("AUDIENCE_XFER_ROOT").get.trim()
  lazy val analytics_root: String = scala.util.Properties.envOrNone("ANALYTICS_ROOT").get.trim()
  lazy val do_supress_warnings: String = scala.util.Properties.envOrNone("SUPRESS_WARNINGS").get.trim()
  lazy val thundr_root: String = analytics_root + "thundr/"
  lazy val default_prefix: String = "p1pcat_prospect"
}