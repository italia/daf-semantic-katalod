package it.almawave.kb.http

import it.almawave.kb.http.providers._
import java.net.InetSocketAddress

import org.slf4j.LoggerFactory

import org.glassfish.jersey.server.ResourceConfig
import org.glassfish.jersey.jackson.JacksonFeature
import org.glassfish.jersey.servlet.ServletContainer

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.handler.HandlerList
import org.eclipse.jetty.server.handler.DefaultHandler
import org.eclipse.jetty.servlet.ServletHolder
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.server.handler.ResourceHandler
import org.eclipse.jetty.server.handler.ContextHandler

import io.swagger.jaxrs.listing.ApiListingResource
import io.swagger.jaxrs.listing.SwaggerSerializers
import io.swagger.jersey.config.JerseyJaxrsConfig

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

  //  import io.swagger.jaxrs2.integration.resources

  // general configurations.......................................
  val resource_config = new ResourceConfig()
    .packages("io.swagger.jaxrs.listing")
    .packages("io.swagger.jaxrs2.integration.resources")
    .packages("it.almawave.kb.http.endpoints")
    .packages("it.almawave.kb.http.providers")
    .packages("it.almawave.kb.catalog.models")
    .registerClasses(
      classOf[ApiListingResource],
      classOf[SwaggerSerializers],
      classOf[JacksonFeature],
      classOf[JacksonScalaProvider],
      classOf[CORSFilter])

  // general configurations.......................................

  // jersey configuration
  val jersey_container = new ServletContainer(resource_config)
  val jersey_holder = new ServletHolder(jersey_container);
  val jersey_context = new ServletContextHandler(ServletContextHandler.SESSIONS)
  jersey_context.setContextPath("/kb/api/v1")
  jersey_context.addServlet(jersey_holder, "/*")

  // swagger ui
  val swagger_ui_handler = new ResourceHandler()
  swagger_ui_handler.setResourceBase("src/main/swagger-ui")
  val swagger_ui_context = new ContextHandler()
  swagger_ui_context.setContextPath("/")
  swagger_ui_context.setResourceBase(".")
  swagger_ui_context.setHandler(swagger_ui_handler)

  // swagger configuration
  val swagger_servlet_config = new JerseyJaxrsConfig
  val swagger_holder = new ServletHolder(jersey_container);
  //  swagger_holder.setInitParameter("swagger.api.version", "1.0.0")
  //  swagger_holder.setInitParameter("openApi.configuration.location", "conf/openapi-configuration.yaml")
  swagger_holder.setInitParameter("swagger.api.basepath", "http://localhost:7777/kb/api/v1/")
  swagger_holder.setInitOrder(2)
  swagger_holder.setServlet(swagger_servlet_config)
  val swagger_context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS)
  //  swagger_context.addServlet(swagger_holder, "/*")

  //  swagger.api.basepath
  //  http://localhost:7777/kb/api/v1/swagger.json

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

// ---------------------------------------------------
// TODO: TEST
// http://localhost:7777/
// http://localhost:7777/kb/api/v1/ontologies
// http://localhost:7777/kb/api/v1/ontologies
// http://localhost:7777/kb/api/v1/swagger.json
// TODO: swagger?
// ---------------------------------------------------