package it.almawave.kb.http.endpoints

import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Produces
import io.swagger.annotations.Tag
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.Api
import javax.ws.rs.Path
import javax.ws.rs.core.MediaType
import javax.ws.rs.PathParam
import org.slf4j.LoggerFactory

@Api(tags = Array("catalog", "vocabularies"))
@Path("/vocabularies")
class VocabulariesEndpoint {

  val logger = LoggerFactory.getLogger(this.getClass)

  // TODO: filters / indexes
  // TODO: add tests

  @Inject
  var loader: CatalogService = null

  @GET
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Tag(name = "catalog, vocabularies")
  @ApiOperation(nickname = "listVocabularies", value = "list of all vocabularies")
  def all() = {
    logger.debug("getting the list of all the vocabularies")
    loader.vocabularies()
  }

  @GET
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Tag(name = "catalog, vocabularies")
  @ApiOperation(nickname = "vocabularyDetails", value = "details of a vocabulary")
  @Path("/{vocabularyID}")
  def details(@PathParam("vocabularyID") id: String) = {
    logger.debug(s"getting the details for vocabulary with id: ${id}")
    loader.vocabularies()
      .filter { item => item.id.equalsIgnoreCase(id) }
      .headOption
  }
  // TEST.EX: POICategoryClassification

}



