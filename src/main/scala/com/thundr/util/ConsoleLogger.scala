package com.thundr.util

import java.time.{ZoneId, ZonedDateTime}
import java.sql.Timestamp
import com.thundr.core.enums.JobStage


trait ConsoleLogger {

  private def timezone: ZoneId = ZoneId.of("America/Los_Angeles")

  def log(stage: JobStage, param_description: String, param_value: String): Unit = {
    val current_time_lazy = Timestamp.from(ZonedDateTime.now(timezone).toInstant)
    println(s"${current_time_lazy} | ${stage.toString}| ${param_description}: ${param_value} \n")
  }
}
