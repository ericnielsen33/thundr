package com.thundr.data.coremodel

import com.thundr.data.{CoreID, DataSource, JSONColumn}
import org.apache.spark.sql.DataFrame

case object fact_conversion_detail
  extends DataSource
  with CoreID
  with JSONColumn {
  override def name: String = "fact_conversion_detail"
  override def namespace: String = "coremodel"
  override def prefix: String = customer_prefix

  override def dimensionalized: DataFrame = this.withAlias
    .join(
      dim_product.dimensionalized,
      dim_product("sku_id") .equalTo(this("sku_id")) &&
      dim_product("brand_id").equalTo((this("brand_id"))),
      "left")
    .drop(dim_product("sku_id"))
    .drop(dim_product("brand_id"))
    .as(name)
}
