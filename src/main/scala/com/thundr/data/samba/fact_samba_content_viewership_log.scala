package com.thundr.data.samba

import com.thundr.data.{CoreID, DataSource}

object fact_samba_content_viewership_log
 extends DataSource
   with SambaBaseAttributes
   with CoreID  {
  override def name: String = "fact_samba_content_viewership_log"
//  override def namespace: String = "samba"
//  override def prefix: String = default_prefix
}
