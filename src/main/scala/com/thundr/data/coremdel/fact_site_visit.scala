package com.thundr.data.coremdel

import com.thundr.data.DataSource
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions._

case object fact_site_visit extends DataSource {
  override def name: String = "fact_site_visit"

  override def namespace: String = "coremodel"

  override def prefix: String = customer_prefix
//rewrite w/ new sytax and drop extra columns action type_id  and online_Event_id... maybe re-alais whole thing too.
  override def dimensionalized: DataFrame = this.read.as(name)
    .join(
      dim_action_type.dimensionalized.as(dim_action_type.name),
      col(s"${dim_action_type.name}.action_type_id") === col(s"${this.name}.action_type_id"),
//      dim_action_type("action_type_id") === this("action_type_id"),
      "left")

    .join(
      dim_online_event.dimensionalized.as(dim_online_event.name),
      col(s"${dim_online_event.name}.online_event_id") === col(s"${this.name}.online_event_id"),
//      dim_online_event("online_event_id") === this("online_event_id"),
      "left")
}
