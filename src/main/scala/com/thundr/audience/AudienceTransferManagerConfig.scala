package com.thundr.audience

case class AudienceTransferManagerConfig(
                                          job_id: String,
                                          job_name: String,
                                          run_id: String,
                                          run_dt: String,
                                          transfer_limit_per_run: Int
                                        )
