package com.thundr.util

import java.time.LocalDate

// Measurment caldar will be constructed of two separate periods, and pre period will be divided into invtervals
// per-period interval assingments to be determined by distribution of purchase freq of population
//consider implications for RFM modeling and synthetic control
case class MeasurementCalendar(start_date: LocalDate, end_date: LocalDate) {

  def days() = {
    end_date.toEpochDay() - start_date.toEpochDay()
  }
}

