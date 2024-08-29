package com.thundr.reporting.activation

import com.thundr.config.SessionProvider
import com.thundr.data.public_works._
import org.apache.spark.sql._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._

case class  ActivationReport (reportable_audiences: Seq[ActivationReportInputItem])
  extends SessionProvider {

  def assembleReport: Dataset[ActivationReportItem] = {
    import session.implicits._
    if(reportable_audiences.isEmpty) session.createDataset(Seq.empty[ActivationReportItem])
    else {
      val reportable_audiences_df = reportable_audiences.toDS()

      val activation_report = reportable_audiences_df.as("reportable_audiences_df")
        .join(
          fact_audience_member.joinHouseholdID(),
          col("reportable_audiences_df.name").equalTo(fact_audience_member("audience")),
          "inner")
        .select(
          fact_audience_member("audience").as("audience_name"),
          col("reportable_audiences_df.dac_id"),
          fact_audience_member.individual_identity_key,
          fact_audience_member("individual_group_identity_key")
        ).as("unique_audience_members")
        .distinct
        .groupBy(col("audience_name"), col("dac_id"))
        .agg(
          count_distinct(col("individual_identity_key")).as("audience_count").cast(IntegerType),
          count_distinct(col("individual_group_identity_key")).as("household_count").cast(IntegerType)
        )

      activation_report.as[ActivationReportItem]
    }
  }
}
