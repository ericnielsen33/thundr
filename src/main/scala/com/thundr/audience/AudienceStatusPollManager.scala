package com.thundr.audience

import java.sql.Timestamp
import java.time.{Instant, ZoneId, ZonedDateTime}
import scala.util.Try
import org.apache.spark.sql.{DataFrame, SaveMode}
import org.apache.spark.sql.functions._
import com.thundr.config.{ConfigProvider, SessionProvider}
import com.thundr.core.Job
import com.thundr.util.ArgParser




object AudienceStatusPollManager
  extends Job[AudienceStatusPollManagerConfig]
  with ConfigProvider
  with SessionProvider {

  lazy val current_time_lazy: Timestamp = {
    val timezone: ZoneId = ZoneId.of("America/Los_Angeles")
    val now: Instant = ZonedDateTime.now(timezone).toInstant
    Timestamp.from(now)
  }

  val fact_lifecyle_event_table_ref: String = s"$customer_prefix.public_works.fact_lifecyle_event"
  val fact_dac_poll_status_table_ref: String = s"${customer_prefix}.public_works.fact_dac_poll_status"

  val fact_lifecyle_event = session.table(fact_lifecyle_event_table_ref)
  val fact_dac_poll_status = session.table(fact_dac_poll_status_table_ref)

  override def collectArgs(args: Array[String]): Try[AudienceStatusPollManagerConfig] =
    Try {
      val parsed = ArgParser.parse(args)
      val config = AudienceStatusPollManagerConfig(
        parsed("job_id"),
        parsed("job_name"),
        parsed("run_id"),
        parsed("run_dt")
      )
      config
    }

  def pollUnfinishedPolls = {

    import  session.implicits._

    val created = fact_dac_poll_status
      .filter(col("status").equalTo("created"))
      .select(col("name"), col("dac_id"))
      .distinct()

    val finished = fact_dac_poll_status
      .filter(col("status").equalTo("finished"))
      .select(col("name"), col("dac_id"))
      .distinct()

    val polls = created.as("created")
      .join(
        finished.as("finished"),
        col("created.dac_id").equalTo(col("finished.dac_id")),
        "leftanti")
      .as[ImpAudienceDAC]
      .collect()
      .toSeq

    polls.foreach { poll =>
      val status = poll.pollStatus
      status.prettyPrint
    }
  }

  override def execute(args: AudienceStatusPollManagerConfig): Try[Unit] = Try {

    println(s"${current_time_lazy} | STAGE START | Polling unfihsined polls\n")
    pollUnfinishedPolls
    println(s"${current_time_lazy} | STAGE FINISH | Polling unfihsined polls\n")
  }

}
