package it.almawave.kb.http.providers

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider
import com.fasterxml.jackson.databind.ObjectMapper
import javax.ws.rs.ext.Provider
import javax.ws.rs.Produces
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import javax.ws.rs.core.MediaType
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.DeserializationFeature
import javax.ws.rs.ext.ContextResolver

@Provider
@Produces(Array(MediaType.APPLICATION_JSON))
class JacksonScalaProvider extends JacksonJaxbJsonProvider with ContextResolver[ObjectMapper] {

  println("\n\nregistered " + this.getClass)

  val mapper = new ObjectMapper()

  mapper
    .registerModule(DefaultScalaModule)
    .configure(SerializationFeature.INDENT_OUTPUT, true)
    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true)
    .setSerializationInclusion(JsonInclude.Include.NON_NULL)
    .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    .configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true)
    .configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true)
    .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
    .configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS, true) // com.fasterxml.jackson.annotation.JsonInclude

  super.setMapper(mapper)

  override def getContext(klasses: Class[_]): ObjectMapper = mapper

}