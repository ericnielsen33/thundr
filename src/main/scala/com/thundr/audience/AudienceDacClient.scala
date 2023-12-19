//package com.thundr.audience
//
//import com.thundr.config.ConfigProvider
//import sttp.client4.{Response => STTPRespnse, StringBody}
//import sttp.model.{Header, MediaType, Uri}
//import sttp.client4.quick._
//
//object AudienceDacClient
//  extends Serializable
//  with ConfigProvider {
//
//  private lazy val dac_api_key = scala.util.Properties.envOrNone("DAC_API_KEY")
//  private lazy val root: String = "https://us-prod-dac-api.publicisspine.io/"
//  def postNewAudience(audience: Audience): STTPRespnse[String] = {
//    val uri = Uri(s"${root}v1/prospect/transfer")
//    val location: String = s"$customer_prefix.audience_xfer.${audience.name}"
//    val headers: Seq[Header] = Seq(
//      Header("Content-Type", "application/json"),
//      Header("x-discovery-access-token", dac_api_key.get))
//    val req = quickRequest
//      .body(StringBody(s"name: $audience.name, location: $location", "UTF-8"))
//      .headers(headers: _*)
//      .post(uri)
//    req.send()
//  }
//  def refreshAudience (audience: Audience, dac_id: String): STTPRespnse[String] = {
//    val uri = Uri(s"${root}v1/prospect/transfer")
//    val location = s"${customer_prefix}.audience_xfer.${audience.name}"
//    val headers: Seq[Header] = Seq(
//      Header("Content-Type", "application/json"),
//      Header("x-discovery-access-token", dac_api_key.get))
//    val req = quickRequest
//      .body(
//        StringBody(
//          s"name: ${audience.name}, location: ${location}, dac_id: ${dac_id}{",
//          "UTF-8", MediaType.TextPlainUtf8))
//      .headers(headers: _*)
//      .post(uri)
//    req.send()
//  }
//  def pollAudienceStatus(dac_id: String): STTPRespnse[String] = {
//    val uri = Uri(s"${root}v1/prospect/transfer/${dac_id}")
//    val headers: Seq[Header] = Seq(
//      Header("Content-Type", "application/json"),
//      Header("x-discovery-access-token", dac_api_key.get))
//    val req = quickRequest
//      .headers(headers: _*)
//      .get(uri)
//    req.send()
//  }
//}
