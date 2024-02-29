package com.thundr.data.coremdel

import com.thundr.data.DataSource
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions._

case object fact_site_visit extends DataSource {
  override def name: String = "fact_site_visit"

  override def namespace: String = "coremodel"

  override def prefix: String = customer_prefix

  def dimensionalized: DataFrame = this.read.as(name)
    .join(
      dim_action_type.read.as(dim_action_type.name),
      col(s"${name}.action_type_id") === col(s"${dim_action_type.name}.action_type_id"),
      "left")
    .join(
      dim_online_event.read.as(dim_online_event.name),
      col(s"${name}.online_event_id") === col(s"${dim_online_event.name}.online_event_id"),
      "left")
}
