package com.thundr.audience

import org.apache.spark.sql.{DataFrame, SparkSession}
case class AudineceCollection[T](
                                  session: SparkSession,
                                  auience_spec: List[T],
                                  audience_builder: T => DataFrame,
                                  naming_convention: T => String,
                                  meta_convention: T => AudienceMetaSchema)
{
  def mkAudience(elem: T): Audience = {
    val seed: DataFrame = audience_builder(elem)
    val name = naming_convention(elem)
    val audience = Audience(session, seed, name)
    audience
  }

  def createAll(): Unit = {
    auience_spec.foreach(
      spec => {
        val audience = mkAudience(spec)
        val meta = meta_convention(spec)
        audience.create(meta)
      }
    )
  }

}



