package com.thundr.data.samba

import com.thundr.config.ConfigProvider

trait SambaBaseAttributes
  extends ConfigProvider {

  def namespace: String = "samba"
  def prefix: String = default_prefix
  def datasource_code: String = "3_SAMBA"

}
