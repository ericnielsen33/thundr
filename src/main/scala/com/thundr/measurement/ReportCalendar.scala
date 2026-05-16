package com.thundr.measurement

import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.sql.Date
import org.apache.spark.sql.DataFrame
import com.thundr.config.SessionProvider


case class CalendarSchema(period_start: LocalDate, period_end: LocalDate)


abstract class ReportCalendar(start_date: LocalDate, end_date: LocalDate)
  extends SessionProvider {

  def getReportDates: Seq[LocalDate] = {
    Iterator.iterate(start_date)(_.plusDays(1))
      .takeWhile(!_.isAfter(end_date))
      .toSeq
  }

  def toDF(dates: Seq[LocalDate], column_ref: String): DataFrame = {
    import session.implicits._
    dates.map(Date.valueOf).toDF(column_ref)
  }

  def duration_days: Long = ChronoUnit.DAYS.between(start_date, end_date)
}

case class WeeklyCalendar(start_date: LocalDate, end_date: LocalDate, report_date_splits: Int = 1)
  extends ReportCalendar(start_date: LocalDate, end_date: LocalDate) {

  override def getReportDates: Seq[LocalDate] = {
    Iterator.iterate(start_date)(_.plusWeeks(report_date_splits))
      .takeWhile(!_.isAfter(end_date))
      .toSeq
  }

  def getCalendar: Seq[CalendarSchema] = {

    val report_dates: Seq[LocalDate] = Iterator.iterate(start_date)(_.plusWeeks(report_date_splits))
      .takeWhile(!_.isAfter(end_date.plusWeeks(report_date_splits)))
      .toSeq

    val report_objects: Seq[CalendarSchema] = {0 to report_dates.length - 2}
      .map(idx => CalendarSchema(report_dates(idx), report_dates(idx + 1).minusDays(1)))

    report_objects
  }

  def getCalendarDF: DataFrame = {
    import session.implicits._
    getCalendar.toDF()
  }
}

object WeeklyCalendar {

  def apply(report_date_splits: Int, retrospective_units: Int, end_date: LocalDate): WeeklyCalendar = {
    val start_date = end_date.minusWeeks(report_date_splits * retrospective_units)
    WeeklyCalendar(start_date, end_date, report_date_splits)
  }

  def apply(start_date: LocalDate, report_date_splits: Int, prospective_units: Int): WeeklyCalendar = {
    val end_date = start_date.plusWeeks(report_date_splits * prospective_units)
    WeeklyCalendar(start_date, end_date, report_date_splits)
  }

  def apply(reference_date: LocalDate, retrospective_units: Int, prospective_units: Int, report_date_splits: Int): WeeklyCalendar = {

    val start_date = reference_date.minusWeeks(retrospective_units * report_date_splits)
    val end_date = reference_date.plusWeeks(prospective_units * report_date_splits)
    WeeklyCalendar(start_date, end_date, report_date_splits)
  }
}