package com.thundr.measurement

case class MeasurementPipeline(
                     pipeline_id: String,
                     pipline_name: String,
                     own_brand_collection_id: String,
                     category_collection_id: String,
                     brand_halo_collection_id: String,
                     impression_group_ids: Array[String],
                     impression_report_dims: Array[String],
                     conversion_report_dims: Array[String],
                     customer_report_dims: Array[String]
                   ) {

}
