package com.thundr.audience

import com.thundr.config.ConfigProvider
import sttp.client4.StringBody
import sttp.model.{Header, MediaType, Uri}
import sttp.client4.quick._
import ujson._
object AudienceDacClient
  extends App with Serializable {

  lazy val dac_api_key: String = scala.util.Properties.envOrNone("DAC_API_KEY").get
  lazy val customer_prefix: String = scala.util.Properties.envOrNone("CUSTOMER_PREFIX").get
  val root: String = "https://us-prod-dac-api.publicisspine.io/"

  def postNewAudience(audience_name: String): String = {
    val location: String = s"$customer_prefix.audience_xfer.${audience_name}"
    val uri: String = s"${root}/v1/prospect/transfer"
    val headers: Seq[Header] = Seq(
      Header("Content-Type", "application/json"),
      Header("x-discovery-access-token", dac_api_key))
    val body = ujson.Obj(
      "name" -> audience_name,
      "location" -> location
    )
    val req = quickRequest
      .post( uri = uri"${uri}")
      .body(ujson.write(body))
      .headers(headers: _*)
    val res = req.send()
    res.body
  }

  def test(person_id: Int): String = {
    val uri = s"https://swapi.dev/api/people/${person_id}"
    val req = quickRequest
      .get(uri = uri"${uri}")
      .header("Content-Type", "application/json")
    val res = req.send()
    res.body
  }
  println(test(2))
}
