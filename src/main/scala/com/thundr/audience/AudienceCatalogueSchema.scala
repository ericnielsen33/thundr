package com.thundr.audience

import java.sql.Date

case class AudienceCatalogueSchema(
                                audience: String,
                                individual_identity_key: String,
                                last_published: Date,
                                start_date: Date,
                                end_date: Date
                              )
