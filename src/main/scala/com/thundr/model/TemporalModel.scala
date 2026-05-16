package com.thundr.model

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions._

import java.time.LocalDate

trait TemporalModel
  extends GeneralizedModel {

  def train_period_start: LocalDate

  def train_period_end: LocalDate

  def score_period_start: LocalDate

  def score_period_end: LocalDate

}
