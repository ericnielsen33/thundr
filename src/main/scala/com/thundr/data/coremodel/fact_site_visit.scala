package com.thundr.data.coremodel

import com.thundr.data.DataSource
import org.apache.spark.sql.DataFrame

case object fact_site_visit
  extends DataSource {
  override def name: String = "fact_site_visit"

  override def namespace: String = "coremodel"

  override def prefix: String = customer_prefix

  override def dimensionalized: DataFrame = this.withAlias
    .join(
      dim_action_type.dimensionalized,
      dim_action_type("action_type_id").equalTo(this("action_type_id"))  &&
      dim_action_type("brand_id").equalTo(this("brand_id")),
      "left")
    .drop(dim_action_type("action_type_id"))
    .drop(dim_action_type("funnel_stage_id"))
    .drop(dim_action_type("brand_id"))
    .as(this.name)
    .join(
      dim_online_event.dimensionalized,
      dim_online_event("online_event_id").equalTo(this("online_event_id")) &&
      dim_online_event("brand_id").equalTo(this("brand_id")),
      "left")
    .drop(dim_online_event("online_event_id"))
    .drop(dim_online_event("brand_id"))
    .as(this.name)

  def withCoreID: DataFrame = this.withAlias
    .join(
      xref_individual_to_child.dimensionalized,
      this("online_identity_key").equalTo(xref_individual_to_child("user_identity_key")) &&
        xref_individual_to_child("user_identity_type_id").equalTo(3),
      "left")
    .as(name)

}
