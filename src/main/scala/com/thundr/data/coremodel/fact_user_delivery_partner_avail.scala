package com.thundr.data.coremodel

import com.thundr.data.DataSource
import org.apache.spark.sql.DataFrame
case object fact_user_delivery_partner_avail
  extends DataSource {
  override def name: String = "fact_user_delivery_partner_avail"

  override def namespace: String = "coremodel"

  override def prefix: String = default_prefix

  override def dimensionalized: DataFrame = this.withAlias
    .join(
      dim_delivery_partner_code.dimensionalized,
      this("delivery_partner_code").equalTo(dim_delivery_partner_code("delivery_partner_code"),
      "left")
    )
}