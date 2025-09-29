package com.thundr.core.enums

sealed trait DatasourceCode {
  def name: String = this.getClass.getName
  override def toString: String = name
}


object DatasourceCodes
  extends EnumUDFMapable[DatasourceCode] {

  case object E_TSP extends DatasourceCode
  case object E_TSP_DEMO extends  DatasourceCode
  case object E_TSP_AUTO extends DatasourceCode
  case object E_TXSPEND extends DatasourceCode
  case object E_CONTEXT extends DatasourceCode
  case object E_CTRANSACT extends DatasourceCode
  case object E_MTRENDS extends DatasourceCode
  case object E_ONLINE extends DatasourceCode
  case object E_SHOP extends DatasourceCode
  case object E_SHOP_HEALTH extends DatasourceCode

  case object CL_EVENT extends DatasourceCode
  case object CL_SITE extends  DatasourceCode

  case object P3_DATAAXLE extends DatasourceCode {
    override def name: String = "3_DATAAXLE"
  }
  case object P3_EXPERIAN_CONSUMER extends DatasourceCode {
    override def name: String = "3_EXPERIAN_CONSUMER"
  }
  case object P3_GWI extends DatasourceCode {
    override def name: String = "3_GWI"
  }
  case object P3_INM_SEGMENTS extends DatasourceCode {
    override def name: String = "3_INM_SEGMENTS"
  }
  case object P3_INM_VISITATION extends DatasourceCode {
    override def name: String = "3_INM_VISITATION"
  }
  case object P3_IRICUSTOM extends DatasourceCode {
    override def name: String = "3_IRICUSTOM"
  }
  case object P3_IRIPRO extends DatasourceCode {
    override def name: String = "3_IRIPRO"
  }
  case object P3_MRI extends DatasourceCode {
    override def name: String = "3_MRI"
  }
  case object P3_NEWVEHICLE extends DatasourceCode {
    override def name: String = "3_NEWVEHICLE"
  }
  case object P3_SAMBA extends DatasourceCode {
    override def name: String = "3_SAMBA"
  }

  def values: Seq[DatasourceCode] = { Seq.empty :+
    E_TSP :+
    E_TSP_DEMO :+
    E_TSP_AUTO :+
    E_TXSPEND :+
    E_CONTEXT :+
    E_CTRANSACT :+
    E_MTRENDS :+
    E_ONLINE :+
    E_SHOP :+
    E_SHOP_HEALTH :+
    CL_EVENT :+
    CL_SITE :+
    P3_DATAAXLE :+
    P3_EXPERIAN_CONSUMER :+
    P3_GWI :+
    P3_INM_SEGMENTS :+
    P3_INM_VISITATION :+
    P3_IRICUSTOM :+
    P3_IRIPRO :+
    P3_MRI :+
    P3_NEWVEHICLE :+
    P3_SAMBA
  }
}
