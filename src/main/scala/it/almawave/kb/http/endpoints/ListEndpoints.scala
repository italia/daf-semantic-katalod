package it.almawave.kb.http.endpoints

import io.swagger.annotations.Api
import javax.ws.rs.Path
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import it.almawave.kb.catalog.ResourcesLoader
import it.almawave.kb.catalog.models.VocabularyMeta
import javax.ws.rs.core.Context
import javax.inject.Singleton
import io.swagger.annotations.ApiOperation
import javax.servlet.http.HttpServletRequest

@Api()
@Path("/vocabularies")
class VocabularyEndpoint {

  // TODO: use dependency injection 
  // CHECK: singleton service?
  // TODO: filters / indexes

  lazy val loader = ResourcesLoader("./conf/catalog.conf")

  @GET
  @Produces(Array(MediaType.APPLICATION_JSON))
  @ApiOperation(nickname = "listVocabularies", value = "list of all vocabularies")
  def all() = {
    loader.fetchVocabularies(false)
  }

}

@Api
@Path("/ontologies")
class OntologiesEndpoint {

  // TODO: use dependency injection 
  // CHECK: singleton service?
  // TODO: filters / indexes

  // CHECK: singleton
  lazy val loader = ResourcesLoader("./conf/catalog.conf")

  @GET
  @Produces(Array(MediaType.APPLICATION_JSON))
  @ApiOperation(nickname = "listOntologies", value = "list of all vocabularies")
  def all(@Context httpRequest: HttpServletRequest) = {

    //    val config_path = httpRequest.getAttribute("configuration").asInstanceOf[String]
    //    println("\n\n....config path", config_path)

    loader.fetchOntologies(false)
  }

}

