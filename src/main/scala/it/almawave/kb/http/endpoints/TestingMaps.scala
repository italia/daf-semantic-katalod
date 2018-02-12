//package it.almawave.kb.http.endpoints
//
//import javax.ws.rs.Produces
//import javax.ws.rs.GET
//import io.swagger.annotations.ApiOperation
//import io.swagger.annotations.Api
//import javax.servlet.http.HttpServletRequest
//import javax.ws.rs.Path
//import javax.ws.rs.core.Context
//import io.swagger.annotations.Tag
//import org.slf4j.LoggerFactory
//import javax.ws.rs.core.MediaType
//import com.typesafe.config.ConfigFactory
//
//// TESTING for different serialization
//@Api
//@Path("/testing/maps")
//class TestingMaps {
//
//  //  first: { uno: 11 due: 22}
//
//  val data = Map(
//    "first" -> Map("uno" -> 11, "due" -> 22),
//    "second" -> Map("true" -> 33, "due" -> 44))
//
//  @GET
//  @Produces(Array(MediaType.APPLICATION_JSON))
//  def example_json(@Context httpRequest: HttpServletRequest) = {
//
//    data
//
//  }
//
//  @GET
//  @Produces(Array("text/csv"))
//  def example_csv(@Context httpRequest: HttpServletRequest) = {
//
//    val csv = data.map { row =>
//      row._2.toList.map { el => s"${row._1}.${el._1};${el._2}" }.mkString(";")
//    }.mkString("\n")
//
//    csv
//
//  }
//
//}
//
//object VerifyCSVSerialization extends App {
//
//  import scala.collection.JavaConversions._
//  import scala.collection.JavaConverters._
//
//  val data = Map(
//    "first" -> Map("uno" -> 11, "due" -> 22),
//    "due" -> Map("uno" -> 33, "due" -> 44))
//
//  val tree = ConfigFactory.parseMap(data)
//
//  tree.root().unwrapped().entrySet().foreach { item =>
//    println(item)
//  }
//
//  //  data.map { row =>
//  //    row._2.toList.map { el => s"${row._1}.${el._1};${el._2}" }.mkString(";")
//  //  }
//  //    .foreach { item => println(item) }
//
//}
//
