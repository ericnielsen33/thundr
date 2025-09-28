package com.thundr.core.services.model_eval

import java.sql.Timestamp

case class ModelEvalSchema(
                            brand_id: Int,
                            project_name: String,
                            model_name: String,
                            run_id: String,
                            trigger_time: Timestamp,
                            start_time: Timestamp,
                            end_time: Timestamp,
                            model_path: String,
                            metrics: Seq[ModelEvalMetric],
                            params: Seq[ModelParam]
                          )
