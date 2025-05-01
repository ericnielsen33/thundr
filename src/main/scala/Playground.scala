import java.io.IOException

import com.thundr.core.enums.JobStages

case class Test(first: String, second: Int) {
  def apply(map: Map[String, String]) = {
    Test(map("first"), map("second").toInt)
  }
}

object Playground extends App {

  val test_map: Map[String, Any] = Map("first" -> "1", "second" -> 2)

  def getCCParams(cc: AnyRef) =
    cc.getClass.getDeclaredFields.foldLeft(Map.empty[String, Any]) { (a, f) =>
      f.setAccessible(true)
      a + (f.getName -> f.get(cc))}

  println(JobStages.STAGE_START.getClass.getSimpleName)
  println(JobStages.STAGE_START)

 }