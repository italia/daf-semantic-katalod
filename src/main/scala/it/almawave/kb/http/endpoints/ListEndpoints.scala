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
import javax.inject.Inject

@Api()
@Path("/vocabularies")
class VocabularyEndpoint {

  // TODO: filters / indexes

  @Inject
  var loader: CatalogService = null

  @GET
  @Produces(Array(MediaType.APPLICATION_JSON))
  @ApiOperation(nickname = "listVocabularies", value = "list of all vocabularies")
  def all() = {
    loader.vocabularies()
  }

}

@Api
@Path("/ontologies")
class OntologiesEndpoint {

  // TODO: filters / indexes

  @Inject
  var loader: CatalogService = null

  @GET
  @Produces(Array(MediaType.APPLICATION_JSON))
  @ApiOperation(nickname = "listOntologies", value = "list of all vocabularies")
  def all(@Context httpRequest: HttpServletRequest) = {
    loader.ontologies()
  }

}

