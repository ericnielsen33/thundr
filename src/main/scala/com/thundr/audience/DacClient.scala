package com.thundr.audience

import org.apache.http.client.methods.{HttpGet, HttpPost}
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.HttpHeaders
import org.apache.http.entity.StringEntity
import upickle.default._
import org.apache.commons.io.IOUtils
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

object DacClient
  extends Serializable {

  lazy val dac_api_key: String = scala.util.Properties.envOrNone("DAC_API_KEY").get
  lazy val customer_prefix: String = scala.util.Properties.envOrNone("CUSTOMER_PREFIX").get
  private val client = HttpClientBuilder.create().build()
  val root: String = "https://us-prod-dac-api.publicisspine.io/"

  def postNewAudience(audience_name: String): String = {
    val location: String = s"$customer_prefix.audience_xfer.${audience_name}"
    val uri: String = s"${root}/v1/prospect/transfer"
    val entity: Map[String, String] = Map("name" -> audience_name, "location" -> location)
    val json: String = write(entity)
    val req = new HttpPost(uri)
    req.addHeader(HttpHeaders.CONTENT_TYPE, "application/json")
    req.addHeader("x-discovery-access-token", dac_api_key)
    req.setEntity(new StringEntity(json))
    val res = client.execute(req)
    IOUtils.toString(res.getEntity().getContent(), StandardCharsets.UTF_8)
  }

  def pollAudienceStatus(dac_id: String): String = {
    val uri: String = s"${root}/v1/transfer/${dac_id}"
    val req = new HttpGet(uri)
    req.addHeader(HttpHeaders.CONTENT_TYPE, "application/json")
    req.addHeader("x-discovery-access-token", dac_api_key)
    val res = client.execute(req)
    IOUtils.toString(res.getEntity().getContent(), Charset.defaultCharset())
  }

  def refreshExistingAudience(audience_name: String, dac_id: String): String = {
    val location: String = s"$customer_prefix.audience_xfer.${audience_name}"
    val uri: String = s"${root}/v1/prospect/transfer"
    val entity: Map[String, String] = Map(
      "name" -> audience_name, "location" -> location, "dac_id" -> dac_id
    )
    val json: String = write(entity)
    val req = new HttpPost(uri)
    req.addHeader(HttpHeaders.CONTENT_TYPE, "application/json")
    req.addHeader("x-discovery-access-token", dac_api_key)
    req.setEntity(new StringEntity(json))
    val res = client.execute(req)
    IOUtils.toString(res.getEntity().getContent(), Charset.defaultCharset())
  }

  def test(person_id: Int): String = {
    val uri = s"https://swapi.dev/api/people/${person_id}"
    val req = new HttpGet(uri)
    req.addHeader(HttpHeaders.CONTENT_TYPE, "application/json")
    val res = client.execute(req)
    IOUtils.toString(res.getEntity().getContent(), Charset.defaultCharset())
  }
}
