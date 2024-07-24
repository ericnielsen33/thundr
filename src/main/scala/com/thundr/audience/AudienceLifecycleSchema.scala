package com.thundr.audience

import java.sql.Timestamp

case class AudienceLifecycleSchema(name: String, timestamp: Timestamp, event: String, collection: Option[String], props_json: Option[String])
