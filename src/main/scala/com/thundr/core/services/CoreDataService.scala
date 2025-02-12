package com.thundr.core.services

import io.delta.tables.DeltaTable
import com.thundr.config._
import org.apache.spark.sql._

abstract class CoreDataService[T]
  extends SessionProvider with ConfigProvider {

  type S <: Dataset[T]

  def catalogue: String = customer_prefix
  def schema: String = "public_works"
  def table_name: String
  def uri: String = s"$catalogue.$schema.$table_name"
  def read: S
  def create: DeltaTable
  def overwrite: CoreDataService[T]
  def append: CoreDataService[T]
  def delete: CoreDataService[T]

}
