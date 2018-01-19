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


// ---------------------------------------------------
// TODO: TEST
// http://localhost:7777/
// http://localhost:7777/kb/api/v1/ontologies
// http://localhost:7777/kb/api/v1/ontologies
// http://localhost:7777/kb/api/v1/swagger.json
// TODO: swagger?
  //  swagger.api.basepath
  //  http://localhost:7777/kb/api/v1/swagger.json

// ---------------------------------------------------