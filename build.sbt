import java.time.LocalDate

val project_major_version = "0.5"
lazy val date = LocalDate.now().toString
ThisBuild / version :=  s"$project_major_version-SNAPSHOT$date"

ThisBuild / scalaVersion := "2.12.14"

artifactName := { (sv: ScalaVersion, module: ModuleID, artifact: Artifact) =>
  artifact.name + "-" + module.revision + "." + artifact.extension
}

val sparkVersion = "3.5.0"
val circeVersion = "0.15.0-M1"

lazy val root = (project in file("."))
  .settings(
    name := "thundr"
  )

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-sql" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-mllib" % sparkVersion % "provided",
  "org.apache.httpcomponents" % "httpclient" % "4.5.14" % "provided",
  "org.apache.httpcomponents" % "httpcore" % "4.4.16" % "provided",
  "io.circe" %% "circe-core" % circeVersion % "provided",
  "io.circe" %% "circe-generic" % circeVersion % "provided",
  "io.circe" %% "circe-parser" % circeVersion % "provided",
  "io.delta" %% "delta-spark" % "3.2.0" % "provided",
  "org.json4s" %% "json4s-core" % "3.7.0-M11",
  ) 