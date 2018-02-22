

package check

import java.net.InetAddress
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._

object TestingIP extends App {

  val test1 = InetAddress.getLoopbackAddress.getHostAddress
  val test2 = InetAddress.getLocalHost.getHostAddress

  println(test1, test2)

}