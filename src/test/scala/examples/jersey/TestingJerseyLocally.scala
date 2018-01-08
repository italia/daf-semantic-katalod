package examples.jersey

import javax.ws.rs._
import java.util.{ Map => JMap, HashMap => JHashMap }
import javax.ws.rs.ApplicationPath
import javax.ws.rs.core.Application
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ServletHolder
import org.eclipse.jetty.servlet.ServletContextHandler
import org.glassfish.jersey.servlet.ServletContainer
import org.glassfish.jersey.server.ServerProperties
import javax.ws.rs.core.GenericEntity
import javax.ws.rs.core.Response
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.DeserializationFeature
import javax.ws.rs.ext.Provider
import org.glassfish.jersey.servlet.ServletContainer
import javax.ws.rs.core.MediaType

/**
 * a simple example.
 * SEE: https://memorynotfound.com/configure-jersey-with-annotations-only/
 *
 * http://localhost:8082/application.wadl
 * http://localhost:8082/courses
 *
 */
object TestingJerseyLocally extends App {

  val server = new Server(8082)

  val servletHolder = new ServletHolder(classOf[org.glassfish.jersey.servlet.ServletContainer])
  servletHolder.setInitParameter("javax.ws.rs.Application", classOf[ApplicationConfig].getName())
  val contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS)

  contextHandler.setContextPath("/")
  contextHandler.addServlet(servletHolder, "/*")
  server.setHandler(contextHandler)

  server.start()
  server.join()

}

@Path("/courses")
class CourseRestService {

  @GET
  @Produces(Array(MediaType.APPLICATION_JSON))
  def all() = {

    val courses = List(
      new Course(1, "Configure Jersey with annotations"),
      new Course(2, "Configure Jersey without web.xml"),
      new Course(4, "Scala introduction"))

    val entity = new GenericEntity(courses, classOf[List[Course]])
    Response.ok(courses).build()

  }

}

case class Course(var id: Integer, var name: String) {
  def getId() = id
  def getName() = name
}

@ApplicationPath("/api")
class ApplicationConfig extends Application {

  import scala.collection.JavaConversions._
  import scala.collection.JavaConverters._

  override def getProperties(): JMap[String, Object] = {
    val properties = new JHashMap[String, Object]
    properties.put(ServerProperties.WADL_FEATURE_DISABLE, "false")
    properties.put(ServerProperties.APPLICATION_NAME, "katalod")
    properties.put(ServerProperties.PROVIDER_PACKAGES, "examples.jersey")
    properties.put(ServerProperties.PROVIDER_SCANNING_RECURSIVE, "true")
    properties
  }

}

@Provider
@Produces(Array(MediaType.APPLICATION_JSON))
class JacksonJsonProviderScala extends JacksonJaxbJsonProvider {

  println("\n\nregistered " + this.getClass)

  val mapper = new ObjectMapper()
  mapper.registerModule(new DefaultScalaModule())
  mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
  mapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT)
  mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
  mapper.configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS, true) // com.fasterxml.jackson.annotation.JsonInclude
  mapper.configure(SerializationFeature.INDENT_OUTPUT, true)
  mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
  super.setMapper(mapper)

}

