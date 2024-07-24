package com.thundr.audience

import java.sql.Timestamp

case class AudienceStatusSchema(name: String, timestamp: Timestamp, status: String, dac_id: String, rqs_id: String)
