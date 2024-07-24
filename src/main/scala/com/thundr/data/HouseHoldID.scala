package com.thundr.data

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions._
import com.thundr.data.coremodel.xref_individual_group_id_to_individual


trait HouseHoldID {

  this: DataSource with CoreID =>

  def joinHouseholdID(hh_type_id: Int = 1) = {
    this.withAlias
      .join(xref_individual_group_id_to_individual.withAlias,
        xref_individual_group_id_to_individual.individual_identity_key.equalTo(this.individual_identity_key) &&
          xref_individual_group_id_to_individual.individual_group_type_id.equalTo(hh_type_id),
        "left")
      .drop(xref_individual_group_id_to_individual.individual_group_type_id)
      .drop(xref_individual_group_id_to_individual.individual_identity_key)
      .as(this.name)
  }
}
