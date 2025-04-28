package com.thundr.core.enums

sealed trait JobStage {
  def name: String = this.getClass.getName
  override def toString: String = name
}


object JobStages
  extends EnumUDFMapable[JobStage] {


  case object JOB_START extends JobStage
  case object COLLECT_ARGS extends  JobStage
  case object VALIDATE_ARGS extends JobStage
  case object EXECUTE_JOB extends JobStage
  case object VALIDATE_OUTPUT extends JobStage
  case object CLEAN_UP extends JobStage
  case object JOB_FINISHED extends JobStage

  def values: Seq[JobStage] = { Seq.empty :+
    JOB_START :+
    COLLECT_ARGS :+
    VALIDATE_ARGS :+
    EXECUTE_JOB :+
    VALIDATE_OUTPUT :+
    CLEAN_UP :+
    JOB_FINISHED
  }
}
