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
                            error_message: String,
                            brands: List[String],
                            data_sources: List[String]
                          )

object DacPollResponse extends Serializable {
  implicit val formats: Formats = Serialization.formats(NoTypeHints)
  def encode(dacPollResponse: DacPollResponse): String = write[DacPollResponse](dacPollResponse)
  def decode(json_str: String) : DacPollResponse = read[DacPollResponse](json_str)
}
