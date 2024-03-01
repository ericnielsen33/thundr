package com.thundr.data.inmarket

import com.thundr.data.DataSource
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions._
case object fact_inmarket_segment
  extends DataSource {
  override def name: String = "fact_inmarket_segment"

  override def namespace: String = "inmarket_audience"

  override def prefix: String = default_prefix

  override def dimensionalized: DataFrame = this.read.as(this.name)
    .join(
      dim_inmarket_segment.dimensionalized.as(dim_inmarket_segment.name),
      col(s"${this.name}.inmarket_segment_id") === col(s"${dim_inmarket_segment.name}.inmarket_segment_id"),
      "left"
    )
}
