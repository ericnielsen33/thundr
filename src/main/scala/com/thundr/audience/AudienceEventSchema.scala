package com.thundr.audience

import java.sql.Timestamp

// events, create, replace, activate, refresh, delete
//Create enum for audience actions
case class AudienceEventSchema(
                          name: String,
                          timestamp: Timestamp,
                          action: String,
                          collection: Option[String] = None
                        )
