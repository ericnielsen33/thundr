package com.thundr.audience

  import java.sql.Timestamp
//An audience can be routine or ad-hoc ( category)
//The program represents the brand (or in the case of PMX) requesting agency
case class AudienceMetaSchema(
                         name: String,
                         inserted_at: Timestamp,
                         refresh_cadence_days: Option[Int] = None,
                         collection: Option[String] = None,
                         ttl_days: Int = 270,
                         premium_datasets: Option[String] = None,
                         dac_id: Option[String] = None
                       )
