package com.thundr.measurement


import java.time.LocalDate
import java.util.UUID
import io.delta.tables._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.DataFrame
import com.thundr.data._
import com.thundr.data.public_works.PublicWorksDataset


case class ImpressionGroupMetaSchema(
                                        group_id: String,
                                        group_name: String,
                                        brand_ref: String,
                                        base_table_ref: String,
                                        insert_date: LocalDate,
                                        last_updated: LocalDate
                                      )
;

case object ImpressionGroupMeta
  extends BaseTable
    with PublicWorksDataset {

  override def prefix: String = customer_prefix

  override def name: String = "impression_group_metadata"

  override def create: DataFrame = {
    session.sql(
      s"""
         |CREATE OR REPLACE TABLE ${this.uri} (
         |                group_id STRING NOT NULL,
         |                group_name STRING NOT NULL,
         |                brand_ref STRING NOT NULL,
         |                base_table_ref STRING NOT NULL,
         |                insert_date DATE NOT NULL,
         |                last_updated DATE NOT NULL,
         |                CONSTRAINT impression_group_metadata_primary_key_constraint
         |                    PRIMARY KEY (group_id)) USING DELTA;
         |""".stripMargin
    )
    this.read.limit(10)
  }

  def append(meta: ImpressionGroupMetaSchema): DataFrame = {
    import session.implicits._
    val df = Seq(meta).toDF()
    this.append(df)
  }

  def insert_new(group_name: String, brand_ref:String, base_table_ref: String): DataFrame = {

    val today: LocalDate = LocalDate.now()
    val group_id: String = UUID.randomUUID().toString

    val meta = ImpressionGroupMetaSchema(
      group_id = group_id,
      group_name = group_name,
      brand_ref = brand_ref,
      base_table_ref = base_table_ref,
      insert_date = today,
      last_updated = today
    )
    this.append(meta)
  }

  def delete_group(group_id: String) = {
    val deltaTable = DeltaTable.forName(session, this.uri)
    deltaTable.delete(col("group_id") === group_id)
  }
}
