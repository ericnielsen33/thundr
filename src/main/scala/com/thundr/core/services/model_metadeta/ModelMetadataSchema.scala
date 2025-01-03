package com.thundr.core.services.model_metadeta

import java.time.LocalDate

case class ModelMetadataSchema(
                                project_name: String,
                                model_name: String,
                                run_id: String,
                                run_date: LocalDate,
                                brand_id: Int,
                                metric_name: String,
                                training_metric_val: Double,
                                test_metric_val: Double,
                                model_path: String
                              )
