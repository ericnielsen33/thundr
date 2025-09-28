package com.thundr.measurement

import java.time.LocalDate
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions._



case class CampaignMeta(
                         name: String,
                         start: LocalDate,
                         end: LocalDate,
                         params: Map[String, Any],
                         selector: Map[String, Any] => DataFrame) {
  def selectRawImpressions: DataFrame = selector(params)

  def preAggregateImpressions(raw_impressions: DataFrame): DataFrame = {
    raw_impressions
      .groupBy(col("online_identity_key"), col("impression_date"))
      .agg(
        sum(col("impression_cnt")).as("impression_cnt")
      )
  }

}
