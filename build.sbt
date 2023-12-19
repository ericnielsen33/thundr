ThisBuild / version := "0.4.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.14"

val sparkVersion = "3.5.0"
val AkkaVersion = "2.8.5"
val AkkaHttpVersion = "10.5.3"

lazy val root = (project in file("."))
  .settings(
    name := "thundr"
  )

resolvers += "Akka library repository".at("https://repo.akka.io/maven")

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-sql" % sparkVersion % "provided",
  "org.apache.httpcomponents" % "httpclient" % "4.5.13",
  "org.apache.httpcomponents" % "httpcore" % "4.4.14",
  "com.lihaoyi" %% "upickle" % "3.1.3",
//  "com.softwaremill.sttp.client4" %% "core" % "4.0.0-M6",
  "io.delta" %% "delta-core" % "2.1.0" % "provided",
  "io.spray" %%  "spray-json" % "1.3.6",
  "org.yaml" % "snakeyaml" % "2.0"
  ) 