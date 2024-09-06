package com.thundr.core.services.dac

import com.thundr.audience.DacPollResponse
import org.apache.commons.io.IOUtils
import org.apache.http.HttpHeaders
import org.apache.http.client.methods.{HttpGet, HttpPost, HttpPut}
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClientBuilder
import org.json4s._
import org.json4s.jackson.Serialization
import java.nio.charset.{Charset, StandardCharsets}


object DacClientV3
  extends Serializable {

  implicit val formats = org.json4s.DefaultFormats
  lazy val dac_api_key: String = scala.util.Properties.envOrNone("DAC").get
  private val client = HttpClientBuilder.create().build()
  val root: String = "https://us-prod-dac-api-v3.publicisspine.io/"

  def postNewAudience(
                       audience_name: String,
                       location: String,
                       data_sources: List[String] = List.empty,
                       brands: List[String] = List.empty
                     ): String = {
    val uri: String = s"${root}v3/transfer"
    val entity: Map[String, Object] = Map(
      "name" -> audience_name,
      "location" -> location,
      "data_sources" -> data_sources,
      "brands" -> brands
    )
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
    val uri: String = s"${root}v3/transfer/${dac_id}"
    val req = new HttpGet(uri)
    req.addHeader(HttpHeaders.CONTENT_TYPE, "application/json")
    req.addHeader("x-discovery-access-token", dac_api_key)
    val res = client.execute(req)
    val json: String = IOUtils.toString(res.getEntity().getContent(), Charset.defaultCharset())
    val dacPollResponse: DacPollResponse = DacPollResponse.decode(json)
    dacPollResponse
  }

  def refreshExistingAudience(
                               audience_name: String,
                               location: String,
                               dac_id: String,
                               data_sources: List[String] = List.empty
                             ): String = {
    val uri: String = s"${root}v3/transfer"
    val entity: Map[String, Object] = Map(
      "name" -> audience_name,
      "location" -> location,
      "dac_id" -> dac_id,
      "data_sources" -> data_sources
    )
    val json: String = Serialization.write(entity)
    val req = new HttpPut(uri)
    req.addHeader(HttpHeaders.CONTENT_TYPE, "application/json")
    req.addHeader("x-discovery-access-token", dac_api_key)
    req.setEntity(new StringEntity(json))
    val res = client.execute(req)
    IOUtils.toString(res.getEntity().getContent(), Charset.defaultCharset())
  }

  def getBrands() : String = {
    val uri: String = s"${root}/v3/client/brands"
    val req = new HttpGet(uri)
    req.addHeader("x-discovery-access-token", dac_api_key)
    val res = client.execute(req)
    IOUtils.toString(res.getEntity().getContent(), Charset.defaultCharset())
  }

  def getDataSources(): String = {
    val uri: String = s"${root}/v3/datasources"
    val req = new HttpGet(uri)
    req.addHeader("x-discovery-access-token", dac_api_key)
    val res = client.execute(req)
    IOUtils.toString(res.getEntity().getContent(), Charset.defaultCharset())
  }
}
