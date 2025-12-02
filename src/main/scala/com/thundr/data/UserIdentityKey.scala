package com.thundr.data

import org.apache.spark.sql.Column

trait UserIdentityKey {
  this: DataSource =>
  def user_identity_key: Column = this("user_identity_key")
  def user_identity_type_id: Column = this("user_identity_type_id")
}
