import java.time.LocalDate

val project_major_version = "0.5"
val date = LocalDate.now().toString
ThisBuild / version :=  s"$project_major_version-SNAPSHOT$date"

ThisBuild / scalaVersion := "2.12.14"

artifactName := { (sv: ScalaVersion, module: ModuleID, artifact: Artifact) =>
  artifact.name + "-" + module.revision + "." + artifact.extension
}

val sparkVersion = "3.5.0"

lazy val root = (project in file("."))
  .settings(
    name := "thundr"
  )

resolvers += "Akka library repository".at("https://repo.akka.io/maven")

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-sql" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-mllib" % sparkVersion % "provided",
  "org.apache.httpcomponents" % "httpclient" % "4.5.14" % "provided",
  "org.apache.httpcomponents" % "httpcore" % "4.4.16" % "provided",
  "org.scala-lang" %% "toolkit" % "0.1.7",
  "io.delta" %% "delta-core" % "2.1.0" % "provided",
  "org.yaml" % "snakeyaml" % "2.0",
  ) 