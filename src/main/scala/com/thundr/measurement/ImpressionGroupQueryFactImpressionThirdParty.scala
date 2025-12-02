package com.thundr.measurement

import org.apache.spark.sql.{DataFrame, Column}
import org.apache.spark.sql.functions._
import com.thundr.data.coremodel.fact_impression_third_party
import com.thundr.data.coremodel.xref_individual_to_child

case class ImpressionGroupQueryFactImpressionThirdParty
  (product_key: String, group_id: String, taxonomy_flat: DataFrame) {


  val impression_feed = {
    fact_impression_third_party.read
      .filter(col("user_identity_type_id").isInCollection(Seq(1,3)))
      .filter(col("vendor_creative_name").contains(s"PR~${product_key}"))
  }

  val enahnced_ids = impression_feed
    .filter(col("user_identity_type_id").equalTo(lit(3)))
    .select(
      col("user_identity_key"),
      col("user_identity_type_id"))
    .dropDuplicates
    .as("impression_feed")
    .join(
      xref_individual_to_child.withAlias,
      col("impression_feed.user_identity_type_id").equalTo(xref_individual_to_child.user_identity_type_id) &&
        col("impression_feed.user_identity_key").equalTo(xref_individual_to_child.user_identity_key),
      "inner")
    .select(
      xref_individual_to_child.individual_identity_key,
      col("impression_feed.user_identity_type_id"),
      col("impression_feed.user_identity_key"),
      lit(1).as("enahanced_user_identity_type_id")
    )
    .filter(xref_individual_to_child.individual_identity_key.isNotNull)
    .dropDuplicates

  val impression_feed_enahnced = impression_feed.as("impression_feed")
    .join(
      enahnced_ids.as("enahnced_ids"),
      col("impression_feed.user_identity_type_id").equalTo(col("enahnced_ids.user_identity_type_id")) &&
        col("impression_feed.user_identity_key").equalTo(col("enahnced_ids.user_identity_key")),
      "inner")
    .select(
      col("impression_date"),
      coalesce(col("enahnced_ids.individual_identity_key"), col("impression_feed.user_identity_key")).as("user_identity_key"),
      coalesce(col("enahnced_ids.enahanced_user_identity_type_id"), col("impression_feed.user_identity_type_id")).as("user_identity_type_id"),
      col("vendor_creative_name"),
      col("impression_cnt"),
      col("total_vendor_cost_usd"))
    .withColumn("taxonomy_map", str_to_map(col("vendor_creative_name"), lit("_"), lit("~")))
    .crossJoin(taxonomy_flat)
    .groupBy(
      col("impression_date"),
      col("user_identity_key"),
      col("user_identity_type_id"),
      extract_tax_member("PR", col("taxonomy_map")).as("product_value"),
      col("vendor_audience_name").as("audience_segment"),
      extract_tax_member("AU", col("taxonomy_map")).as("audience_type"),
      extract_tax_member("PB", col("taxonomy_map")).as("publisher"),
      col("taxonomy_map")("FF").as("campaign_name")
    )
    .agg(
      sum(col("impression_cnt")).as("impression_cnt"),
      sum(col("total_vendor_cost_usd")).as("media_cost")
    )
    .select(
      lit(group_id).as("group_id"),
      col("impression_date"),
      col("user_identity_key"),
      col("user_identity_type_id"),
      map(taxonomy: _*).as("report_dimensions"),
      col("impression_cnt"),
      col("media_cost")
    )

  val taxonomy = reporting_taxonomy_domains.flatMap(elem => Seq(lit(elem), col(elem)))

    def reporting_taxonomy_domains: Seq[String] = {
      Seq("product_value", "audience_segment", "audience_type", "publisher", "campaign_name")
    }



    def extract_tax_member(domain_key: String, taxonomy_map: Column): Column = {
      coalesce(
        col(s"${domain_key}.domain_members")(taxonomy_map(domain_key)),
        taxonomy_map(domain_key)
      )
    }
  }
