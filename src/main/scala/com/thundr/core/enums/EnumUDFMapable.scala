package com.thundr.core.enums


trait EnumUDFMapable {
  case object UNMATCHED extends Serializable {
    def name: String = this.getClass.getName
  }

  def values: Seq[Serializable]

  def unmatched_value: Serializable = UNMATCHED
}
