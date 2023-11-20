//package com.thundr.audience
//
//import sttp.client3.{Response, StringBody}
//import sttp.model.{Header, MediaType, Uri}
//import sttp.client3.quick.{simpleHttpClient, quickRequest}
//object AudienceDacClient  {
//  private val root: String = "https://us-prod-dac-api.publicisspine.io/"
//  def postNewAudience(prefix: String, audience: Audience, dac_token: String): Response[String] = {
//    val uri = Uri(s"${root}v1/prospect/transfer")
//    val location: String = s"$prefix.audience_xfer.${audience.name}"
//    val headers: Seq[Header] = Seq(
//      Header("Content-Type", "application/json"),
//      Header("x-discovery-access-token", dac_token))
//    val req = quickRequest
//      .body(StringBody(s"name: $audience.name, location: $location", "UTF-8"))
//      .headers(headers: _*)
//      .post(uri)
//    simpleHttpClient.send(req)
//  }
//  def refreshAudience (prefix: String, audience: Audience, dac_token: String, dac_id: String): Response[String] = {
//    val uri = Uri(s"${root}v1/prospect/transfer")
//    val location = s"${prefix}.audience_xfer.${audience.name}"
//    val headers: Seq[Header] = Seq(
//      Header("Content-Type", "application/json"),
//      Header("x-discovery-access-token", dac_token))
//    val req = quickRequest
//      .body(StringBody(s"name: ${audience.name}, location: ${location}", "UTF-8", MediaType.TextPlainUtf8))
//      .headers(headers: _*)
//      .post(uri)
//    simpleHttpClient.send(req)
//  }

//  def pollAudienceStatus(prefix: String, audience: Audience, dac_token: String, dac_id: String): Response[String] = {
//    val uri = Uri(s"${root}v1/prospect/transfer/${dac_id}")
//    val headers: Seq[Header] = Seq(
//      Header("Content-Type", "application/json"),
//      Header("x-discovery-access-token", dac_token))
//    val req = quickRequest
//      .headers(headers: _*)
//      .get(uri)
//    simpleHttpClient.send(req)
//  }
//}
