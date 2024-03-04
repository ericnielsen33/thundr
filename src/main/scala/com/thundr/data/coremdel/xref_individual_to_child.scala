package com.thundr.data.coremdel

import com.thundr.data.DataSource
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions._

case object xref_individual_to_child
  extends DataSource {
  override def name: String = "xref_individual_to_child"

  override def namespace: String = "coremodel"

  override def prefix: String = customer_prefix

  override def dimensionalized: DataFrame = this.withAlias
    .join(
      dim_user_identity_type.dimensionalized,
      dim_user_identity_type("user_identity_type_id") === this("user_identity_type_id"),
      "left"
    )
}
