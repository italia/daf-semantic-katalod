package it.almawave.kb.http

import org.slf4j.LoggerFactory
import org.glassfish.jersey.server.ResourceConfig
import io.swagger.jaxrs.listing.ApiListingResource
import io.swagger.jaxrs.listing.SwaggerSerializers
import org.glassfish.jersey.jackson.JacksonFeature
import it.almawave.kb.http.providers._
import org.eclipse.jetty.server.Server
import java.net.InetSocketAddress
import org.eclipse.jetty.server.handler.HandlerList
import org.eclipse.jetty.server.handler.DefaultHandler
import org.glassfish.jersey.servlet.ServletContainer
import org.eclipse.jetty.servlet.ServletHolder
import org.eclipse.jetty.servlet.ServletContextHandler
import io.swagger.jersey.config.JerseyJaxrsConfig
import org.eclipse.jetty.server.handler.ResourceHandler
import org.eclipse.jetty.server.handler.ContextHandler

object MainHTTP extends App {

  val http = HTTP(
    "localhost", 7777,
    "/kb/api/v1")

  // TODO: load configurations

  http.start

}

object HTTP {

  def apply(host: String, port: Int, base: String) = new HTTP(host, port, base)

}

class HTTP(host: String, port: Int, base: String) {

  private val logger = LoggerFactory.getLogger(this.getClass)

  val server = new Server(new InetSocketAddress("localhost", 7777))

  // general configurations.......................................
  val rc = new ResourceConfig()
    .packages("io.swagger.jaxrs.listing")
    .packages("it.almawave.kb.http.endpoints")
    .packages("it.almawave.kb.catalog.models")

  rc.registerClasses(
    classOf[ApiListingResource],
    classOf[SwaggerSerializers],
    classOf[JacksonFeature],
    classOf[JacksonScalaProvider],
    classOf[CORSFilter])

  // general configurations.......................................

  // TODO: TEST
  // http://localhost:7777/
  // http://localhost:7777/kb/api/v1/ontologies
  // http://localhost:7777/kb/api/v1/ontologies
  // http://localhost:7777/kb/api/v1/swagger.json
  // TODO: swagger?

  // .............................................................

  // jersey configuration
  val jersey_container = new ServletContainer(rc)
  val jersey_holder = new ServletHolder(jersey_container);
  val jersey_context = new ServletContextHandler(ServletContextHandler.SESSIONS)
  jersey_context.setContextPath("/kb/api/v1")
  //  jersey_context.setAttribute("resources_path", "http://localhost:7777/static")
  //  jersey_context.setAttribute("resources_base", "./data")
  jersey_context.addServlet(jersey_holder, "/*")
  
  // testing configurations
//  jersey_context.setAttribute("configuration", "./conf/catalog.conf")

  // swagger ui
  val swagger_ui_handler = new ResourceHandler()
  swagger_ui_handler.setWelcomeFiles(Array("index.html"))
  swagger_ui_handler.setResourceBase("src/main/swagger-ui")
  val swagger_ui_context = new ContextHandler()
  swagger_ui_context.setContextPath("/")
  swagger_ui_context.setResourceBase(".")
  swagger_ui_context.setHandler(swagger_ui_handler)

  // swagger configuration
  val swagger_servlet_config = new JerseyJaxrsConfig
  val swagger_holder = new ServletHolder(jersey_container);
  swagger_holder.setInitParameter("api.version", "1.0.0")
  swagger_holder.setInitParameter("swagger.api.basepath", "http://localhost:7777/kb/api/v1/")
  swagger_holder.setInitOrder(2)
  swagger_holder.setServlet(swagger_servlet_config)
  val swagger_context = new ServletContextHandler(ServletContextHandler.SESSIONS)
  swagger_context.addServlet(swagger_holder, "/*")

  val handlers = new HandlerList()
  handlers.setHandlers(Array(
    jersey_context,
    swagger_ui_context,
    new DefaultHandler()))

  server.setHandler(handlers)

  def start {
    logger.info(s"START")
    server.start()
    server.join()
  }

  def stop {
    logger.info(s"STOP")
    server.stop()
  }

}
