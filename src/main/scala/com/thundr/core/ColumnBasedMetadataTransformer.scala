//package com.thundr.core
//import com.thundr.data.DataSource
//import org.apache.spark.sql.DataFrame
//
// case class ColumnBasedMetadataTransformer
//  (dataSource: DataSource, metadataSource: DataSource, dataSourceCols: Seq[String] = Seq())
//   extends Transformer {
//    def this(dataSource: DataSource, metadataSource: DataSource) =
//      this(dataSource, metadataSource, dataSource.read.columns.toSeq)
//    def filterColumns(predicate: String => Boolean): ColumnBasedMetadataTransformer =
//      ColumnBasedMetadataTransformer(dataSource, metadataSource, dataSourceCols.filter(predicate))
//
//   override def transformer(item: Nothing, input: DataFrame): DataFrame = {
//     val input_alias = "input"
//     val transformed = input.as(input_alias)
//
//
//     transformed
//   }
//   override def transformations[T]: Seq[T] = ???
//
//}
