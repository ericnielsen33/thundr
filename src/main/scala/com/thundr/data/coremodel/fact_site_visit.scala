package com.thundr.data.coremodel

import com.thundr.data.DataSource
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions._

case object fact_site_visit extends DataSource {
  override def name: String = "fact_site_visit"

  override def namespace: String = "coremodel"

  override def prefix: String = customer_prefix
//rewrite w/ new sytax and drop extra columns action type_id  and online_Event_id... maybe re-alais whole thing too.
  override def dimensionalized: DataFrame = this.withAlias
    .join(
      dim_action_type.dimensionalized,
      dim_action_type("action_type_id") === this("action_type_id"),
      "left")
    .as(this.name)
    .join(
      dim_online_event.dimensionalized,
      dim_online_event("online_event_id") === this("online_event_id"),
      "left")
}
