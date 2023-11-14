import com.thundr.audience.Audience
import com.thundr.config.SessionProvider
import org.apache.spark.sql.DataFrame

//--add-exports java.base/sun.nio.ch=ALL-UNNAMED
case class AudienceMember(individual_identity_key: String)

object Playground
  extends App
  with SessionProvider
   {
     import sparkSession.implicits._

     val d1: DataFrame = Seq(
       AudienceMember("a"),
       AudienceMember("b"),
       AudienceMember("c"),
       AudienceMember("d"),
       AudienceMember("e")
     ).toDF()

     val d2: DataFrame = Seq(
       AudienceMember("a"),
       AudienceMember("d"),
       AudienceMember("e"),
       AudienceMember("f")
     ).toDF()

    val a1 = Audience(sparkSession, d1, "a1")
    val a2 = Audience(sparkSession, d2, "a2")
    val a3 = a1.union(a2)
    println {a3.contains(a2)}

}

