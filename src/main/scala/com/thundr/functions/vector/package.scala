package com.thundr.functions

import org.apache.spark.ml.linalg.Vector
import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.functions.udf

package object vector {

  private def cosineSimilarity(v1: Vector, v2: Vector): Double = {
    val dotProduct = v1.dot(v2)
    val magnitude1 = math.sqrt(v1.toArray.map(x => x * x).sum)
    val magnitude2 = math.sqrt(v2.toArray.map(x => x * x).sum)
    if (magnitude1 == 0 || magnitude2 == 0) 0.0
      else {
        dotProduct / ( magnitude1 * magnitude2 )
      }
  }

  val cosine_similarity: UserDefinedFunction = udf((v1: Vector, v2: Vector) => cosineSimilarity(v1, v2))

  val extract_value_from_vector: Int => UserDefinedFunction = (index: Int) => udf((v: Vector) => v(index))

}
