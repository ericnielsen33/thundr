package com.thundr.data.samba

import com.thundr.data.{CoreID, DataSource}

case object fact_samba_exposure_log
 extends DataSource
   with SambaDataSet
   with CoreID {

  override def name: String = "fact_samba_exposure_log"
  override def prefix: String = default_prefix
}
