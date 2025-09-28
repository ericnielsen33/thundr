package com.thundr.model.classification

import java.time.LocalDateTime

case class BinaryClassifcationModelParmset(
                                          brand_id: Int,
                                          project_name: String,
                                          model_name: String,
                                          feature_table_ref: String,
                                          seed_ref: String,
                                          run_id: String,
                                          run_dt: String
                                          )
