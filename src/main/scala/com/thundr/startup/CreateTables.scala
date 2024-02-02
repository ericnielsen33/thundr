package com.thundr.startup

import com.thundr.core.Job
import com.thundr.audience.{
  AudienceCatalogueProvider, AudienceLifecycleProvider, AudienceMetaProvider, AudienceStatusProvider
}
import org.apache.spark.sql.SparkSession

object CreateTables
 extends Job {

  lazy val session: SparkSession = SparkSession.getActiveSession.get
  private val audienceCatalogueProvider: AudienceCatalogueProvider = new AudienceCatalogueProvider(session)
  private val audienceMetaProvider: AudienceMetaProvider = new AudienceMetaProvider(session)
  private val audienceStatusProvider: AudienceStatusProvider = new AudienceStatusProvider((session))
  private val audienceLifecycleProvider: AudienceLifecycleProvider = new AudienceLifecycleProvider(session)
  def main(args: Array[String]): Unit = execute(Map("" -> ""))
  override def collectArgs(args: Array[String]): Map[String, String] = ???
  override def execute(args: Map[String, String]): Unit = {
    audienceCatalogueProvider.create()
    audienceMetaProvider.create()
    audienceStatusProvider.create()
    audienceLifecycleProvider.create()
  }
}
