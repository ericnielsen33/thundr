package com.thundr.audience

import org.apache.spark.sql.{DataFrame, SparkSession}
case class AudineceCollection[T](
                                  session: SparkSession,
                                  audience_spec: List[T],
                                  audience_builder: T => DataFrame,
                                  naming_convention: T => String)
{

  private var audiences: Array[Audience] = Array()
  def mkAudience(elem: T): Audience = {
    val seed: DataFrame = audience_builder(elem)
    val name = naming_convention(elem)
    val audience = Audience(session, seed, name)
    audience
  }
  def generateAll(): Unit = ???
  def xferAll(): Unit = ???
}



