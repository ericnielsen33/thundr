package com.thundr.util

import java.time.{Instant, ZoneId, ZonedDateTime}
import java.sql.Timestamp
import com.thundr.core.enums.JobStage


trait ConsoleLogger {

  private lazy val current_time_lazy: Timestamp = {
    val timezone: ZoneId = ZoneId.of("America/Los_Angeles")
    val now: Instant = ZonedDateTime.now(timezone).toInstant
    Timestamp.from(now)
  }

  def log(stage: JobStage, param_description: String, param_value: String): Unit ={
    println(s"${current_time_lazy} | ${stage.toString}| ${param_description}: ${param_value} \n")
  }
}
