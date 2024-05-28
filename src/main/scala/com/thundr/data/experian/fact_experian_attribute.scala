package com.thundr.data.experian

import com.thundr.data.{CoreID, DataSource}
import org.apache.spark.sql.DataFrame
case object fact_experian_attribute
  extends DataSource
  with CoreID {
  override def name: String = "fact_experian_attribute"

  override def namespace: String = "experian"

  override def prefix: String = default_prefix

  override def dimensionalized: DataFrame = this.withAlias
    .join(
      dim_experian_attribute.withAlias,
      (dim_experian_attribute("experian_attribute_id").equalTo(this("experian_attribute_id"))) &&
        (dim_experian_attribute("experian_attribute_value").equalTo(this("experian_attribute_value"))),
      "left")
    .drop(dim_experian_attribute("experian_attribute_id"))
    .drop(dim_experian_attribute("experian_attribute_value"))
    .drop(this("experian_identity_key"))
}