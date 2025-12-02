package com.thundr.measurement

import com.thundr.data.DataSource
import org.apache.spark.sql.{Column, DataFrame}
import org.apache.spark.sql .functions._
import com.thundr.data.coremodel._

import java.time.LocalDate

case class ImpressionGroupQueryFactImpression(
                                             group_id: String,
                                             dimensions: Map[String, Column],
                                             report_week: LocalDate,
                                             query_start: LocalDate,
                                             query_end: LocalDate
                                             )
  extends ImpressionGroupQuery {

  override def data_source: DataSource = fact_impression

  override def execute: DataFrame = {

//    consider partitioning by a report period (eg weekly) to make backfills easy
//    will need to add separate transformation to keep track of map using hashing or coverting to other primitive type.
    fact_impression.read
      .filter(col("impression_date").between(lit(query_start), lit(query_end)))
//      .xref_join
      .select(
        lit(group_id).as("group_id"),
        col("impression_date"),
        col("user_identity_key").as("online_identity_key"),
        lit(3).as("user_identity_type_id"),
        col("individual_identity_key")
      )

  }
}
