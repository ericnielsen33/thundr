package com.thundr.data.inmarket

import com.thundr.data.DataSource
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions._
case object fact_inmarket_segment
  extends DataSource {
  override def name: String = "fact_inmarket_segment"

  override def namespace: String = "inmarket_audience"

  override def prefix: String = default_prefix

  override def dimensionalized: DataFrame = this.withAlias
    .join(
      dim_inmarket_segment.dimensionalized,
      dim_inmarket_segment("inmarket_segment_id").equalTo(this("inmarket_segment_id")),
      "left"
    )
}
