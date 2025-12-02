package com.thundr.data.coremodel

import com.thundr.data.{DataSource, JSONColumn, OnlineID}
import org.apache.spark.sql.DataFrame

case object fact_impression
  extends DataSource
    with OnlineID
    with JSONColumn{
  override def name: String = "fact_impression"
  override def namespace: String = "coremodel"
  override def prefix: String = customer_prefix
  def xref_join: DataFrame = {
    this.withAlias.join(
      xref_individual_to_child.withAlias,
      this.online_identity_key.equalTo(xref_individual_to_child("user_identity_key")) &&
        xref_individual_to_child("user_identity_type_id").equalTo(3)
    )
  }

}