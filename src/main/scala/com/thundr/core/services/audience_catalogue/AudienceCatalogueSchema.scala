package com.thundr.core.services.audience_catalogue

import java.sql.Date

case class AudienceCatalogueSchema(
                                audience: String,
                                individual_identity_key: String,
                                last_published: Date,
                                start_date: Date,
                                end_date: Date
                              )
