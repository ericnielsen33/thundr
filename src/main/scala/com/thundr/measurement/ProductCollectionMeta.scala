package com.thundr.measurement

import java.time.LocalDate
import java.util.UUID
import io.delta.tables._
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions._
import com.thundr.data._


case class ProductCollectionMetaSchema(
                                      collection_id: String,
                                      collection_name: String,
                                      brand_ref: String,
                                      conversion_table_ref: String,
                                      dimension_table_ref: String,
                                      collection_type: String,
                                      insert_date: LocalDate,
                                      last_updated: LocalDate
                                      )
;

case object ProductCollectionMeta
  extends BaseTable with public_works.PublicWorksDataset {

  override def prefix: String = customer_prefix

  override def name: String = "product_collection_metadata"

  override def create: DataFrame = {
    session.sql(
      s"""
         |CREATE OR REPLACE TABLE ${this.uri} (
         |                collection_id STRING NOT NULL,
         |                collection_name STRING NOT NULL,
         |                brand_ref STRING NOT NULL,
         |                conversion_table_ref STRING NOT NULL,
         |                dimension_table_ref STRING NOT NULL,
         |                collection_type STRING NOT NULL,
         |                insert_date DATE NOT NULL,
         |                last_updated DATE NOT NULL,
         |                CONSTRAINT products_metadata_primary_key_constraint
         |                    PRIMARY KEY (collection_id)) USING DELTA;
         |""".stripMargin
    )
    this.read.limit(10)
  }

  def add_constraints(): Unit = {
    session.sql(
      s"""
         |ALTER TABLE ${this.uri}
         |ADD CONSTRAINT ${this.name}_collection_type_enum_constraint
         |CHECK (collection_type IN ('OWN_BRAND', 'BRAND_HALO', 'CATEGORY'))
         |;
         |""".stripMargin
    )
  }

  def append(meta: ProductCollectionMetaSchema): DataFrame = {
    import session.implicits._
    val df = Seq(meta).toDF()
    this.append(df)
  }

  def append(collection_name: String, brand_ref: String, conversion_table_ref: String, dimension_table_ref: String, collection_type: String): DataFrame = {

    val today: LocalDate = LocalDate.now()
    val collection_id: String = UUID.randomUUID().toString
    val meta: ProductCollectionMetaSchema = ProductCollectionMetaSchema(
      collection_id = collection_id,
      collection_name = collection_name,
      brand_ref = brand_ref,
      conversion_table_ref = conversion_table_ref,
      dimension_table_ref = dimension_table_ref,
      collection_type = collection_type,
      insert_date = today,
      last_updated = today
    )
    this.append(meta)
  }

  def read_collection(collection_id: String): DataFrame = this.read.filter(col("collection_id").equalTo(lit(collection_id)))

  def delete_collection(collection_id: String) = {
    val deltaTable = DeltaTable.forName(session, this.uri)
    deltaTable.delete(col("collection_id") === collection_id)
  }

  def update_collection(collection: ProductCollectionMetaSchema) = {
    import  session.implicits._
    val deltaTable = DeltaTable.forName(session, this.uri)
    val updates_df = Seq(collection).toDF()

    deltaTable.as("target")
      .merge(
        updates_df.as("updates"),
        "target.id = updates.id")
      .whenMatched()
      .update(
        Map(
          "collection_id" -> col("target.collection_id"),
          "collection_name" -> col("updates.collection_name"),
          "brand_ref" -> col("updates.brand_ref"),
          "conversion_table_ref" -> col("updates.conversion_table_ref"),
          "dimension_table_ref" -> col("updates.dimension_table_ref"),
          "collection_type" -> col("updates.collection_type"),
          "insert_date" -> col("target.insert_date"),
          "last_updated" -> current_date()))
      .whenNotMatched()
      .insert(
        Map(
          "collection_id" -> col("updates.collection_id"),
          "collection_name" -> col("updates.collection_name"),
          "brand_ref" -> col("updates.brand_ref"),
          "conversion_table_ref" -> col("updates.conversion_table_ref"),
          "dimension_table_ref" -> col("updates.dimension_table_ref"),
          "collection_type" -> col("updates.collection_type"),
          "insert_date" -> current_date(),
          "last_updated" -> current_date())
      )
      .execute()


  }

}
