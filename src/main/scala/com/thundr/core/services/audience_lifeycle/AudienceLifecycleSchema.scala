package com.thundr.core.services.audience_lifeycle

import java.sql.Timestamp

case class AudienceLifecycleSchema(name: String, timestamp: Timestamp, event: String, collection: Option[String], props_json: Option[String])
