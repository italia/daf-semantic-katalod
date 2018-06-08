package it.almawave.kb.http.endpoints

import io.swagger.annotations.Api
import javax.ws.rs.Path
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Context
import javax.inject.Singleton
import io.swagger.annotations.ApiOperation
import javax.servlet.http.HttpServletRequest
import javax.inject.Inject
import io.swagger.annotations.Tag
import org.slf4j.LoggerFactory
import javax.ws.rs.PathParam
import scala.collection.Map

@Api(tags = Array("catalog"))
@Path("/ontologies")
class OntologiesEndpoint {

  val logger = LoggerFactory.getLogger(this.getClass)

  // TODO: filters / indexes
  // TODO: add tests

  @Inject
  var loader: CatalogService = null

  @GET
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Tag(name = "catalog, ontologies")
  @ApiOperation(nickname = "listOntologies", value = "list of all ontologies")
  def all(@Context httpRequest: HttpServletRequest) = {
    logger.debug("getting the list of all the ontologies")
    loader.ontologies()
  }

  @GET
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Tag(name = "catalog, ontologies")
  @ApiOperation(nickname = "ontologyDetails", value = "details of an ontology")
  @Path("/{ontologyID}")
  def details(@PathParam("ontologyID") id: String) = {
    logger.debug(s"getting the details for ontology with id: ${id}")
    loader.ontologies_aligns()
      .filter { item => item.id.equalsIgnoreCase(id) }
      .headOption
  }
  // TEST.EX: IoT-AP_IT

}

