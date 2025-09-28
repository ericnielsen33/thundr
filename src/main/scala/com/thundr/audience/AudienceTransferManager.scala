package com.thundr.audience

import scala.collection.mutable.ListBuffer
import java.time.{Instant, ZoneId, ZonedDateTime}
import java.sql.Timestamp
import scala.util.Try
import org.apache.spark.sql.{DataFrame, SaveMode}
import org.apache.spark.sql.functions._
import com.thundr.config.{ConfigProvider, SessionProvider}
import com.thundr.util.{ArgParser, ConsoleLogger}
import com.thundr.core.Job
import com.thundr.core.enums.JobStages.{PARAM_READ, STAGE_END, STAGE_START, TASK_END}


case class DacInitialTransferSpec(job_request_id: Long, job_name: String, audience_id: String, name: String, data_sources: List[String], brands: List[String])

case class JobRequestFulfillment(job_request_id: Long, completed_at: Timestamp, job_name: String, job_id: String, run_id: String)

object AudienceTransferManager
  extends Job[AudienceTransferManagerConfig]
  with SessionProvider
  with ConfigProvider
  with ConsoleLogger {

  val timezone: ZoneId = ZoneId.of("America/Los_Angeles")

  val fact_audience_member_table_ref = s"$customer_prefix.audience_xfer.ad_alchemy_audience_member_scd1"
  val fact_job_request_table_ref = s"$customer_prefix.audience_xfer.ad_alchemy_job_requests"
  val fact_job_fulfillments_table_ref = s"$customer_prefix.audience_xfer.ad_alchemy_job_fulfillments"
  val fact_lifecyle_event_table_ref = s"$customer_prefix.public_works.fact_lifecyle_event"

  val fact_audience_member = session.table(fact_audience_member_table_ref)
  val fact_job_request = session.table(fact_job_request_table_ref)
  val fact_job_fulfillments = session.table(fact_job_fulfillments_table_ref)
  val fact_lifecyle_event = session.table(fact_lifecyle_event_table_ref)

  override def collectArgs(args: Array[String]): Try[AudienceTransferManagerConfig] = Try {

        val parsed = ArgParser.parse(args)

        parsed.keys.foreach { key => log(PARAM_READ, key, parsed(key)) }

        val config = AudienceTransferManagerConfig(
          parsed("job_id"),
          parsed("job_name"),
          parsed("run_id"),
          parsed("run_dt"),
          parsed("transfer_limit_per_run").toInt
        )
      config
    }

  def collectPendingTransfers(args: AudienceTransferManagerConfig ): Seq[DacInitialTransferSpec] = {
    import session.implicits._

    val audience_catalog_params = fact_audience_member
      .select(
        col("audience_id"),
        col("audience_name"))
      .distinct()

    val cancelations = fact_job_request
      .filter(col("job_name").equalTo("CANCEL_JOB"))
      .select(
        col("job_request_id"),
        col("request_params.cancel_job_params.job_request_id").as("cancelation_job_request_id"),
        col("requested_at"),
        col("requested_by_email")
      )

    val refreshable_audiences = fact_lifecyle_event
      .filter(col("event").equalTo("TRANSFER_OK"))
      .select(col("name"))
      .distinct

    val pending_transfers_df: DataFrame = fact_job_request
      .filter(col("job_name").equalTo("TRANSFER_NEW_AUDIENCE"))
      .select(
        col("job_request_id"),
        col("job_name"),
        col("requested_at"),
        col("audience_id"),
        col("request_params.transfer_params.name").as("name"),
        col("request_params.transfer_params.data_sources").as("data_sources"),
        col("request_params.transfer_params.brands").as("brands")
      )
      .as("transfer_requests")
      .join(
        cancelations.as("cancelations"),
        col("transfer_requests.job_request_id").equalTo(col("cancelations.cancelation_job_request_id")) &&
          col("cancelations.requested_at").gt(col("transfer_requests.requested_at")),
        "leftanti")
      .join(
        fact_job_fulfillments.as("fulfillments").select(col("job_request_id")).distinct,
        col("transfer_requests.job_request_id").equalTo(col("fulfillments.job_request_id")),
        "leftanti")
      .join(
        audience_catalog_params.as("audience_catalog_params"),
        col("transfer_requests.audience_id").equalTo(col("audience_catalog_params.audience_id")),
        "inner")
      .join(
        refreshable_audiences.as("refreshable_audiences"),
        coalesce(col("transfer_requests.name"), col("audience_catalog_params.audience_name")).equalTo(col("refreshable_audiences.name")),
        "leftanti"
      )
      .select(
        col("transfer_requests.job_request_id").as("job_request_id"),
        col("transfer_requests.job_name").as("job_name"),
        col("transfer_requests.audience_id").as("audience_id"),
        coalesce(col("transfer_requests.name"), col("audience_catalog_params.audience_name")).as("name"),
        col("transfer_requests.data_sources").as("data_sources"),
        col("transfer_requests.brands").as("brands")
      )

    val pending_transfers_seq: Seq[DacInitialTransferSpec] = pending_transfers_df
      .as[DacInitialTransferSpec]
      .collect.toSeq
      .take(args.transfer_limit_per_run)

    pending_transfers_seq
  }

  def transfer_audiences(config: AudienceTransferManagerConfig, specs: Seq[DacInitialTransferSpec]): List[ImpAudienceDAC] = {

    import session.implicits._

    val transfers: ListBuffer[ImpAudienceDAC] = ListBuffer[ImpAudienceDAC]()

    val audience_template: DacInitialTransferSpec => ImpAudienceInitialTransferSCD1Catalogued = (spec: DacInitialTransferSpec) => {
      ImpAudienceInitialTransferSCD1Catalogued(
        spec.name,
        spec.audience_id,
        spec.data_sources
      )}

    specs.take(config.transfer_limit_per_run).foreach { spec =>
        val audience: ImpAudienceInitialTransferSCD1Catalogued = audience_template(spec)

        val transfer: ImpAudienceDAC = audience
          .persistXfer
          .activateToDiscovery

      log(TASK_END, "Transfer for job_request_id:", spec.job_request_id.toString)

        transfers.append(transfer)

        val fulfillmnent_time =  Timestamp.from(ZonedDateTime.now(timezone).toInstant)

        val fulfillment = JobRequestFulfillment(
          spec.job_request_id,
          fulfillmnent_time,
          spec.job_name,
          config.job_id,
          config.run_id
        )

        val fulfillments_df: DataFrame = Seq(fulfillment).toDF()

        fulfillments_df
          .write
          .format("delta")
          .mode(SaveMode.Append)
          .saveAsTable(fact_job_fulfillments_table_ref)

      log(TASK_END, "Write fulfillment for job_request_id:", spec.job_request_id.toString)
      }

  transfers.toList
  }

  def poll_transfer_status(transfers: List[ImpAudienceDAC]): List[DacPollResponse] = {

    val statuses: ListBuffer[DacPollResponse] = ListBuffer[DacPollResponse]()
    transfers.foreach { transfer =>
      val status: DacPollResponse = transfer.pollStatus
      statuses.append(status)
      println(status.prettyPrint)

      log(TASK_END, "Poll DAC status for audience_name:", transfer.audience_name)
    }
    statuses.toList
  }

  override def execute(args: AudienceTransferManagerConfig): Unit =  {

    log(STAGE_START, "COLLECTING", "PENDING_TRANSFERS")
    val pending_transfers = collectPendingTransfers(args)
    log(STAGE_END, "COLLECTING", "PENDING_TRANSFERS")
    log(STAGE_START, "EXECUTING", "DAC TRANSFERS")
    val transfers = transfer_audiences(args, pending_transfers)
    log(STAGE_END, "EXECUTING", "DAC TRANSFERS")
    log(STAGE_START, "POLLING", "DAC API")
    val statuses = poll_transfer_status(transfers)
    statuses.foreach(_.prettyPrint)
    log(STAGE_END, "POLLING", "DAC API")
  }
}
