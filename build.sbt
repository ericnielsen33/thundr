ThisBuild / version := "0.4.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.15"

val sparkVersion = "3.5.0"
val AkkaVersion = "2.9.0"
val AkkaHttpVersion = "10.6.0"

lazy val root = (project in file("."))
  .settings(
    name := "thundr"
  )

resolvers += "Akka library repository".at("https://repo.akka.io/maven")

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-sql" % sparkVersion % "provided",
  "com.softwaremill.sttp.client3" %% "core" % "3.9.1",
  "dev.zio" %% "zio-http" % "3.0.0-RC3",
  "dev.zio" %% "zio" % "2.0.15",
  "dev.zio" %% "zio-json" % "0.6.2",
  "io.delta" %% "delta-core" % "2.1.0",
  "io.spray" %%  "spray-json" % "1.3.6",
  "org.yaml" % "snakeyaml" % "2.0"
  ) 