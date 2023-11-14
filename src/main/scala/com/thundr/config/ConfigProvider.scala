package com.thundr.config

import com.thundr.core.enums.CustomerPrefix
import com.thundr.util.Yaml._

trait ConfigProvider {
  lazy val configuration: scala.collection.mutable.Map[String, String] = readFile("conf.yaml")
  lazy val customerPrefix: CustomerPrefix = CustomerPrefix(configuration.getOrElse("customer_prefix", "p1pcat_prospect"))
  lazy val defualtPrefix: CustomerPrefix = CustomerPrefix(configuration.getOrElse("default_prefix", "p1pcat_prospect"))
}
