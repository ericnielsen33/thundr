package com.thundr.core.services.dac

import org.json4s._
import org.json4s.jackson.Serialization
import org.json4s.jackson.Serialization.{read, write}

case class DataSourceCode(id: Int, epsilon_classification_code: String)

object DataSourceCodesResponse extends Serializable {
  implicit val formats: Formats = Serialization.formats(NoTypeHints)
  def encode(entity: List[DataSourceCode]): String = write[List[DataSourceCode]](entity)
  def decode(json_str: String): List[DataSourceCode] = read[List[DataSourceCode]](json_str)
}
