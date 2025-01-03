package com.thundr.data

import org.apache.spark.sql.Column

trait OnlineID {
    this: DataSource =>
    def individual_identity_key: Column = this("online_identity_key")
}
