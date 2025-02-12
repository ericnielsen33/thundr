package com.thundr.audience

import scala.collection.mutable.ListBuffer
import java.time.{Instant, ZoneId, ZonedDateTime}
import java.sql.Timestamp
import scala.util.Try
import org.apache.spark.sql.{DataFrame, SaveMode}
import org.apache.spark.sql.functions._
import com.thundr.config.{ConfigProvider, SessionProvider}
import com.thundr.util.ArgParser
import com.thundr.core.Job


case class DacInitialTransferSpec(job_request_id: Long, job_name: String, audience_id: String, name: String, data_sources: List[String])

case class JobRequestFulfillment(job_request_id: Long, completed_at: Timestamp, job_name: String, job_id: String, run_id: String)

object AudienceTransferManager
  extends Job[AudienceTransferManagerConfig]
  with SessionProvider
  with ConfigProvider {

  lazy val current_time_lazy: Timestamp = {
    val timezone: ZoneId = ZoneId.of("America/Los_Angeles")
    val now: Instant = ZonedDateTime.now(timezone).toInstant
    Timestamp.from(now)
  }

  val fact_audience_member_table_ref = s"$customer_prefix.audience_xfer.fact_audience_member"
  val fact_job_request_table_ref = s"$customer_prefix.audience_xfer.fact_job_request"
  val fact_job_fulfillments_table_ref = s"$customer_prefix.public_works.fact_job_fulfillments"
  val fact_lifecyle_event_table_ref = s"$customer_prefix.public_works.fact_lifecyle_event"

  val fact_audience_member = session.table(fact_audience_member_table_ref)
  val fact_job_request = session.table(fact_job_request_table_ref)
  val fact_job_fulfillments = session.table(fact_job_fulfillments_table_ref)
  val fact_lifecyle_event = session.table(fact_lifecyle_event_table_ref)

  override def collectArgs(args: Array[String]): Try[AudienceTransferManagerConfig] =
    Try {
      val parsed = ArgParser.parse(args)
      val transferManagerConfig = AudienceTransferManagerConfig(
        parsed("job_id"),
        parsed("job_name"),
        parsed("run_id"),
        parsed("run_dt"),
        parsed("transfer_limit_per_run").toInt
      )
      transferManagerConfig
    }

  def collectPendingTransfers: Seq[DacInitialTransferSpec] = {
    import session.implicits._

    println("starting to collect pending transfers")

    val pending_transfers_df: DataFrame = fact_job_request
      .filter(col("job_name").equalTo("TRANSFER_NEW_AUDIENCE"))
      .select(
        col("job_request_id"),
        col("job_name"),
        col("request_params.audience_id").as("audience_id"),
        col("request_params.name").as("name"),
        col("data_sources"))
      .as("transfer_requests")
      .join(
        fact_job_fulfillments.as("fulfillments").select(col("job_request_id")).distinct,
        col("transfer_requests.job_request_id").equalTo(col("fulfillments.job_request_id")),
        "leftanti")
      .join(
        fact_lifecyle_event
          .filter(col("event").equalTo("TRANSFER_OK"))
          .as("fact_lifecyle_event")
          .select(col("name"))
          .distinct,
        col("transfer_requests.name").equalTo(col("fact_lifecyle_event.name")),
        "leftanti")

    val pending_transfers_seq: Seq[DacInitialTransferSpec] = pending_transfers_df
      .as[DacInitialTransferSpec]
      .collect.toSeq
    println("finsiehd collecting pending transfers")
    pending_transfers_seq
  }

  def transfer_audiences(config: AudienceTransferManagerConfig, specs: Seq[DacInitialTransferSpec]): List[ImpAudienceDAC] = {
    import  session.implicits._

    val transfers: ListBuffer[ImpAudienceDAC] = ListBuffer[ImpAudienceDAC]()

    val audience_df_template: DacInitialTransferSpec => DataFrame = (spec: DacInitialTransferSpec) => {
      fact_audience_member
        .filter(col("audience_id").equalTo(spec.audience_id))
        .select(col("individual_identity_key"))
        .distinct
    }

    val audience_template: DacInitialTransferSpec => ImpAudienceInitialTransferSeeded = (spec: DacInitialTransferSpec) => {
      ImpAudienceInitialTransferSeeded(
        spec.name,
        audience_df_template(spec),
        spec.data_sources
      )}
    println("starting to excecute transfers")
    specs.take(config.transfer_limit_per_run).foreach { spec =>
        val audience: ImpAudienceInitialTransferSeeded = audience_template(spec)

        val transfer: ImpAudienceDAC = audience
          .createOrReplaceInCatalogue
          .persistXfer
          .activateToDiscovery

      println { "transfer: " + transfer}

        transfers.append(transfer)

        val fulfillment = JobRequestFulfillment(
          spec.job_request_id,
          current_time_lazy,
          spec.job_name,
          config.job_id,
          config.run_id
        )

      println { "fulfuillment: " + fulfillment}

        val fulfillments_df: DataFrame = Seq(fulfillment).toDF()

        fulfillments_df
          .write
          .format("delta")
          .mode(SaveMode.Append)
          .saveAsTable(fact_job_fulfillments_table_ref)
      }
    println("finished executing transfers")
  transfers.toList
  }

  def poll_transfer_status(transfers: List[ImpAudienceDAC]): List[DacPollResponse] = {
    val statuses: ListBuffer[DacPollResponse] = ListBuffer[DacPollResponse]()
    transfers.foreach { transfer =>
      val status: DacPollResponse = transfer.pollStatus
      statuses.append(status)
    }
    statuses.toList
  }

  override def execute(args: AudienceTransferManagerConfig): Try[Unit] = Try {

    println(args)
    val pending_transfers = collectPendingTransfers
    pending_transfers.foreach(println)
    val transfers = transfer_audiences(args, pending_transfers)
    val statuses = poll_transfer_status(transfers)
    statuses.foreach(println)
  }
}
