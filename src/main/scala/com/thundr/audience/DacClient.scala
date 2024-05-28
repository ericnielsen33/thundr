package com.thundr.audience

import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import org.apache.http.client.methods.{HttpGet, HttpPost}
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.HttpHeaders
import org.apache.http.entity.StringEntity
import org.apache.commons.io.IOUtils
import org.json4s.jackson.Serialization
import org.json4s.jackson.JsonMethods._
import org.json4s._


object DacClient
    extends Serializable {

  implicit val formats = org.json4s.DefaultFormats
  lazy val dac_api_key: String = scala.util.Properties.envOrNone("DAC").get
  private val client = HttpClientBuilder.create().build()
  val root: String = "https://us-prod-dac-api.publicisspine.io/"

  def postNewAudience(audience_name: String, location: String): String = {
    val uri: String = s"${root}/v1/prospect/transfer"
    val entity: Map[String, String] = Map("name" -> audience_name, "location" -> location)
    val json: String = Serialization.write(entity)
    val req = new HttpPost(uri)
    req.addHeader(HttpHeaders.CONTENT_TYPE, "application/json")
    req.addHeader("x-discovery-access-token", dac_api_key)
    req.setEntity(new StringEntity(json))
    val res = client.execute(req)
    IOUtils.toString(res.getEntity().getContent(), StandardCharsets.UTF_8)
  }

  def pollAudienceStatus(dac_id: String): DacPollResponse = {
    implicit val formats: Formats = org.json4s.DefaultFormats
    val uri: String = s"${root}/v1/transfer/${dac_id}"
    val req = new HttpGet(uri)
    req.addHeader(HttpHeaders.CONTENT_TYPE, "application/json")
    req.addHeader("x-discovery-access-token", dac_api_key)
    val res = client.execute(req)
    val json: String = IOUtils.toString(res.getEntity().getContent(), Charset.defaultCharset())
//    val parsed = parse(json)
    val dacPollResponse: DacPollResponse = DacPollResponse.decode(json)
    dacPollResponse
  }
  def refreshExistingAudience(audience_name: String, location: String, dac_id: String): String = {
    val uri: String = s"${root}/v1/prospect/transfer"
    val entity: Map[String, String] = Map(
      "name" -> audience_name, "location" -> location, "dac_id" -> dac_id
    )
    val json: String = Serialization.write(entity)
    val req = new HttpPost(uri)
    req.addHeader(HttpHeaders.CONTENT_TYPE, "application/json")
    req.addHeader("x-discovery-access-token", dac_api_key)
    req.setEntity(new StringEntity(json))
    val res = client.execute(req)
    IOUtils.toString(res.getEntity().getContent(), Charset.defaultCharset())
  }
}
