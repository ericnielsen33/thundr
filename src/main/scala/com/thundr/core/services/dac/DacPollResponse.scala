package com.thundr.audience

import org.json4s._
import org.json4s.jackson.Serialization
import org.json4s.jackson.Serialization.{read, write}


/// add pretty print
case class DacPollResponse(
                            name: String,
                            location: String,
                            status: String,
                            dac_id: String,
                            rqs_id: String,
                            error_message: Option[String] = Option(""),
                            brands: Option[List[String]] = Option(List("")),
                            data_sources: Option[List[String]] = Option(List(""))
                          )

object DacPollResponse extends Serializable {
  implicit val formats: Formats = Serialization.formats(NoTypeHints)
  def encode(dacPollResponse: DacPollResponse): String = write[DacPollResponse](dacPollResponse)
  def decode(json_str: String) : DacPollResponse = read[DacPollResponse](json_str)
}
