package com.thundr.data.samba

import com.thundr.data.DataSource

object fact_samba_exposure_log
 extends DataSource {

  override def name: String = "fact_samba_exposure_log"
  override def namespace: String = "samba"
  override def prefix: String = default_prefix

}
