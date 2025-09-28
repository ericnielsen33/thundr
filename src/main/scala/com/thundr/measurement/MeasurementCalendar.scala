package com.thundr.measurement

import java.time.{DayOfWeek, LocalDate, ZoneId}

case class EncodedDate(date: LocalDate)
case class MeasurementCalendar(start_date: LocalDate, end_date: LocalDate, dates: Seq[LocalDate] = Seq()) {

  private val zoneId = ZoneId.systemDefault()

  def duration_days: Long = end_date.toEpochDay() - start_date.toEpochDay()

  def with_daily_calendar: MeasurementCalendar = {
    val dailyCalendar = {start_date.toEpochDay until end_date.toEpochDay + 1}
      .map(day => LocalDate.ofEpochDay(day))
    MeasurementCalendar(start_date, end_date, dailyCalendar)
  }
  def with_interval_days(interval: Int): MeasurementCalendar = {
    if(dates.isEmpty) {
      with_daily_calendar.with_interval_days(interval)
    } else {
      val filtered_dates = dates.filter(date => (date.toEpochDay - start_date.toEpochDay) % interval == 0)
      MeasurementCalendar(start_date, end_date, filtered_dates)
    }
  }
  def with_start_dow_after_start_date(dow: DayOfWeek): MeasurementCalendar = {
    if(dates.isEmpty) {
      with_daily_calendar.with_start_dow_after_start_date(dow)
    } else {
      val nearestStart = {start_date.toEpochDay until start_date.toEpochDay + 7}
        .map(day => LocalDate.ofEpochDay(day))
        .find(_.getDayOfWeek.equals(dow))
        .getOrElse(this.start_date)

      val filtered_dates = dates.filter(date => date.isAfter(nearestStart) || date.equals(nearestStart))
      MeasurementCalendar(nearestStart, this.end_date, filtered_dates)
    }
  }
  def with_end_dow_before_end_date(dow: DayOfWeek): MeasurementCalendar = {
    if(dates.isEmpty) {
      with_daily_calendar.with_end_dow_before_end_date(dow)
    } else {
      val nearestEnd = {end_date.toEpochDay - 6 until end_date.toEpochDay + 1}
        .map(day => LocalDate.ofEpochDay(day))
        .find(_.getDayOfWeek.equals(dow))
        .getOrElse(start_date)

      val filtered_dates = dates.filter(date => date.isBefore(nearestEnd) || date.equals(nearestEnd))
      MeasurementCalendar(start_date, nearestEnd, filtered_dates)
    }
  }
  def encoded(): Seq[EncodedDate] = {
    val encodedDates = dates
      .map(date => EncodedDate(date))
    encodedDates
  }
}