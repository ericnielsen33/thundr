package com.thundr.core.enums

sealed trait JobStage {
  def name: String = this.getClass.getName
  override def toString: String = name
}

object JobStages
  extends EnumUDFMapable[JobStage] {

  case object JOB_START extends JobStage
  case object PARAM_READ extends JobStage
  case object STAGE_START extends  JobStage
  case object TASK_START extends JobStage
  case object TASK_ERROR extends JobStage
  case object TASK_END extends JobStage
  case object STAGE_END extends JobStage
  case object JOB_END extends JobStage

  def values: Seq[JobStage] = { Seq.empty :+
    JOB_START :+
    PARAM_READ :+
    STAGE_START :+
    TASK_START :+
    TASK_ERROR :+
    TASK_END :+
    STAGE_END :+
    JOB_END
  }
}
