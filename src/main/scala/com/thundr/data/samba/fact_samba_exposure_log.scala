package com.thundr.data.samba

import com.thundr.data.{CoreID, DataSource}

object fact_samba_exposure_log
 extends DataSource
 with CoreID {

  override def name: String = "fact_samba_exposure_log"
  override def namespace: String = "samba"
  override def prefix: String = default_prefix

}
