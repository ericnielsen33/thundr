package com.thundr.data.shoppersvoice

import com.thundr.data.DataSource
case object dim_shoppers_voice_metadata
  extends DataSource {

  override def name: String = "dim_shoppers_voice_metadata"

  override def namespace: String = "samba"

  override def prefix: String = default_prefix

}