package com.thundr.data.coremdel

import com.thundr.data.DataSource
import org.apache.spark.sql.DataFrame

case object fact_conversion_detail
  extends DataSource {
  override def name: String = "fact_conversion_detail"
  override def namespace: String = "coremodel"
  override def prefix: String = customer_prefix

  override def dimensionalized: DataFrame = this.withAlias
    .join(
      dim_product.dimensionalized,
      dim_product("sku_id") === this("sku_id"),
      "left"
    )
}
