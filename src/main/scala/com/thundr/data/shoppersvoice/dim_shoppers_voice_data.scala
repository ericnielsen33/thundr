package com.thundr.data.shoppersvoice

import com.thundr.data.DataSource
case object dim_shoppers_voice_data
  extends DataSource {

  override def name: String = "dim_shoppers_voice_data"

  override def namespace: String = "shoppersvoice"

  override def prefix: String = default_prefix

}