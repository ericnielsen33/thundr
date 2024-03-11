package com.thundr.data.etx

import com.thundr.data.DataSource
case object dim_etx_metadata
  extends DataSource {
  override def name: String = "dim_etx_metadata"

  override def namespace: String = "etx"

  override def prefix: String = default_prefix
}

//What if we flattened this table before joining by turning the metadata_group_name into a column
// and aggegating a hashmap of all the the k,v pairs?
//Then to dimensionalize the fact table we cross join and replace each column with a struct?