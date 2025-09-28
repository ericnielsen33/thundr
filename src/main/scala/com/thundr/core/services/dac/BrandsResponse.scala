package com.thundr.core.services.dac

import org.json4s._
import org.json4s.jackson.Serialization
import org.json4s.jackson.Serialization.{read, write}

case class Brand(name: String, slug: String)
case class BrandsResponse(client_name: String, client_slug: String, brands: Option[List[Brand]])

object BrandsResponse extends Serializable {
  implicit val formats: Formats = Serialization.formats(NoTypeHints)
  def encode(response: BrandsResponse): String = write[BrandsResponse](response)
  def decode(json_str: String): BrandsResponse = read[BrandsResponse](json_str)
}
