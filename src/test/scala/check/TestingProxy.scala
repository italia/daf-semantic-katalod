package check

import java.net.ProxySelector
import java.net.URI
import java.net.InetAddress

object TestingProxy extends App {

  import scala.collection.JavaConverters._
  import scala.collection.JavaConversions._

  val host_loopback = InetAddress.getLoopbackAddress.getHostName

  val host_default = InetAddress.getLocalHost.getCanonicalHostName

  println(s"""HOST LOOPBACK:\t${host_loopback}""")
  println(s"""HOST DEFAULT:\t${host_default}""")

}