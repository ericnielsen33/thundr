package com.thundr.audience

import org.json4s._
import org.json4s.jackson.Serialization
import org.json4s.jackson.Serialization.{read, write}

case class DacPostNewAudienceResponse(dac_id: String)

object DacPostNewAudienceResponse extends Serializable {
  implicit val formats: Formats = Serialization.formats(NoTypeHints)

  def encode(response: DacPostNewAudienceResponse): String = write[DacPostNewAudienceResponse](response)

  def decode(json_str: String): DacPostNewAudienceResponse = read[DacPostNewAudienceResponse](json_str)
}
