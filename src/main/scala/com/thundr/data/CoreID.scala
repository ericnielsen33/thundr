package com.thundr.data

import org.apache.spark.sql.Column

trait CoreID {

  this: DataSource =>
  def individual_identity_key: Column = this("individual_identity_key")
}
