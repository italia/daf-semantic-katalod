package it.almawave.kb.http

import org.eclipse.jetty.server.Server
import org.glassfish.jersey.server.ResourceConfig
import org.glassfish.jersey.servlet.ServletContainer
import org.eclipse.jetty.servlet.ServletHolder
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.server.handler.ResourceHandler
import org.eclipse.jetty.server.handler.ContextHandler
import org.eclipse.jetty.server.handler.HandlerList
import org.eclipse.jetty.server.handler.DefaultHandler
import io.swagger.jaxrs.listing.ApiListingResource
import io.swagger.jaxrs.listing.SwaggerSerializers
import io.swagger.jersey.config.JerseyJaxrsConfig
import io.swagger.jaxrs.listing.ApiListingResource
import io.swagger.jaxrs.listing.SwaggerSerializers
import io.swagger.jaxrs.listing.ApiListingResource
import io.swagger.jaxrs.listing.SwaggerSerializers
import io.swagger.jaxrs.listing.ApiListingResource
import io.swagger.jaxrs.listing.SwaggerSerializers
import org.glassfish.jersey.jackson.JacksonFeature
import javax.ws.rs.core.FeatureContext
import java.io.File
import java.net.InetSocketAddress

import it.almawave.kb.http.providers.JacksonProviderScala
import it.almawave.kb.http.providers.CORSFilter

object HTTP {

  // TODO: builder pattern
  def apply(
    host: String,
    port: Int,
    base: String,
    packages: String*) =
    new HTTP(host, port, base, packages: _*)

}

class HTTP(host: String, port: Int, base: String, packages: String*) {

  val server = new Server(new InetSocketAddress(host, port))

  //  val HOST = "localhost"
  val BASE_PATH = s"http://${host}:${port}/${base}"

  val rc = new ResourceConfig()
    .packages("io.swagger.jaxrs.listing")
  packages.foreach { p => rc.packages(p) }

  rc.registerClasses(
    classOf[ApiListingResource],
    classOf[SwaggerSerializers],
    classOf[JacksonFeature],
    classOf[JacksonProviderScala],
    classOf[CORSFilter])

  // jersey API
  val jersey_container = new ServletContainer(rc)
  val jersey_holder = new ServletHolder(jersey_container);
  val jersey_context = new ServletContextHandler(ServletContextHandler.SESSIONS)
  jersey_context.setContextPath("/api")
  jersey_context.setAttribute("resources_path", "http://localhost:7777/static")
  jersey_context.setAttribute("resources_base", "./data")
  jersey_context.addServlet(jersey_holder, "/*")

  // TODO: configurable static context path!

  // static context
//  val static_handler = new ResourceHandler()
//  static_handler.setDirAllowed(true)
//  static_handler.setDirectoriesListed(true)
//  static_handler.setWelcomeFiles(Array("index.html"))
//  // TODO: enable if it's useful to address resource directly!
//  //  static_handler.setResourceBase("./data")
//  val static_context = new ContextHandler()
//  static_context.setContextPath("/static")
//  static_context.setResourceBase(".")
//  static_context.setHandler(static_handler)

  // swagger configuration
  val swagger_servlet_config = new JerseyJaxrsConfig
  val swagger_holder = new ServletHolder(jersey_container);
  swagger_holder.setInitParameter("api.version", "1.0.0")
  swagger_holder.setInitParameter("swagger.api.basepath", BASE_PATH)
  swagger_holder.setInitOrder(2)
  swagger_holder.setServlet(swagger_servlet_config)
  val swagger_context = new ServletContextHandler(ServletContextHandler.SESSIONS)
  swagger_context.addServlet(swagger_holder, "/*")

  // swagger UI
  val swagger_ui_handler = new ResourceHandler()
  swagger_ui_handler.setWelcomeFiles(Array("index.html"))
  swagger_ui_handler.setResourceBase("src/main/swagger-ui")
  val swagger_ui_context = new ContextHandler()
  swagger_ui_context.setContextPath("/")
  swagger_ui_context.setResourceBase(".")
  swagger_ui_context.setHandler(swagger_ui_handler)

  // handlers setup
  val handlers = new HandlerList()
  handlers.setHandlers(Array(
//    static_context,
    jersey_context,
    swagger_ui_context,
    swagger_context,
    new DefaultHandler()))

  server.setHandler(handlers)

  def start = {
    server.start()
    server.join()
  }

  def stop = {
    server.stop()
  }

}