package com.thundr.data.coremdel

import com.thundr.data.DataSource
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions._

case object xref_individual_to_child
  extends DataSource {
  override def name: String = "xref_individual_to_child"

  override def namespace: String = "coremodel"

  override def prefix: String = customer_prefix

  def dimensionalized: DataFrame = this.read.as(name)
    .join(
      dim_user_identity_type.read.as(dim_user_identity_type.name),
      col(s"${name}.user_identity_type_id") === col(s"${dim_user_identity_type.name}.user_identity_type_id"),
      "left"
    )
}
