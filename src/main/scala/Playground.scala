import com.thundr.util.MeasurementCalendar

import java.time.{LocalDate, DayOfWeek, Period}
import com.thundr.data.coremodel.fact_conversion_detail

object Playground extends App {

  val start = LocalDate.of(2023, 12, 1)
  val end = LocalDate.of(2024, 1, 31)
  val calendar = MeasurementCalendar(start, end)
  val nearestEnd = {end.toEpochDay - 6 until end.toEpochDay + 1}
    .map(day => LocalDate.ofEpochDay(day))
    .find(_.getDayOfWeek.equals(DayOfWeek.SUNDAY))
    .get

  val nearestStart = {start.toEpochDay until start.toEpochDay + 7}
    .map(day => LocalDate.ofEpochDay(day))
    .find(_.getDayOfWeek.equals(DayOfWeek.MONDAY))
    .get

  println(fact_conversion_detail.individual_identity_key)

}