package com.thundr.data

object Schema {
  case class Dim_User(
                     brand_id: String,
                     customer_identity_key: String,
                     city_name: String,
                     region_code: String,
                     country_code: String,
                     dma_code: String,
                     dma_name: String,
                     fips_code: String,
                     county_name: String,
                     timezone_name: String,
                     postal_code: String,
                     gender_code: String,
                     conversant_last_seen_day_code: String,
                     user_email_known_flag: Int,
                     user_email_in_network_30_day_flag: Int,
                     individual_identity_key: String,
                     birth_month_nbr: Int,
                     birth_year_nbr: Int,
                     customer_identity_type_id: Int,
                     do_not_mask: Int
                     )
}
