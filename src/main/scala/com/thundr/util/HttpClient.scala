package com.thundr.util

import zio._
import zio.http._

object HttpClient extends ZIOAppDefault {
  val url = "https://swapi.dev/api/people/1"
  // Construct headers
  val headers = Headers(Header.Host("swapi.dev.api.com"), Header.Accept(MediaType.application.json))

  val program = for {
    // Pass headers to request
    res <- Client.request(Request.get(url).addHeaders(headers))
    // List all response headers
    _ <- Console.printLine(res.headers.toList.mkString("\n"))
    data <-
      // Check if response contains a specified header with a specified value.
      if (res.header(Header.ContentType).exists(_.mediaType == MediaType.application.json))
        res.body.asString
      else
        res.body.asString
    _ <- Console.printLine(data)
  } yield ()

  override def run: ZIO[Any, Throwable, Unit] =
    program.provide(Client.default, Scope.default)

}
