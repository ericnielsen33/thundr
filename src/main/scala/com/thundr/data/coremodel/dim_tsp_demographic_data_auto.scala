package com.thundr.data.coremodel

import com.thundr.data.{CoreID, DataSource}
import org.apache.spark.sql.DataFrame

case object dim_tsp_demographic_data_auto
 extends DataSource
   with TSPAUTODataset
   with CoreID {

  def name: String = "dim_tsp_demographic_data_auto"
  def prefix: String = default_prefix

//  override def dimensionalized: DataFrame = this.withAlias
//    .join(
//      dim_demographic_metadata.withAlias,
//
//    )
}
