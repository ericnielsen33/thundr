package com.thundr.util

import org.yaml.snakeyaml.Yaml

import java.io.{File, FileInputStream}
import scala.collection.JavaConverters._

object FileConfBuilder {
  private val mapper: Yaml = new Yaml()
  def readFile(fileName: String): scala.collection.mutable.Map[String, String] = {
    val stream = new FileInputStream(new File(fileName))
    mapper.load(stream).asInstanceOf[java.util.Map[String, String]].asScala
  }
}