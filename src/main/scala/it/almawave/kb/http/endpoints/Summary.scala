package it.almawave.kb.http.endpoints

import javax.ws.rs.Path
import io.swagger.annotations.Api
import javax.ws.rs.core.Context
import javax.servlet.http.HttpServletRequest
import javax.ws.rs.Produces
import javax.ws.rs.GET
import javax.inject.Inject
import javax.ws.rs.core.MediaType
import io.swagger.annotations.ApiOperation

@Api(tags = Array("catalog"))
@Path("/summary")
class Summary {

  @Inject
  var loader: CatalogService = null

  @GET
  @Produces(Array(MediaType.APPLICATION_JSON))
  @ApiOperation(nickname = "listOntologies", value = "list of all ontologies")
  def all(@Context httpRequest: HttpServletRequest) = {

    val ontologies = loader._ontologies.map(_.id)
    val vocabularies = loader._vocabularies.map(_.id)

    s"""{
      
      "ontologies": {
      
        "size": ${ontologies.size},
        "items": [
          ${ontologies.map(o => s""""${o}"""").mkString(", ")}
        ]
      
      },
      
      "vocabularies": {
      
        "size": ${vocabularies.size},
        "items": [
          ${vocabularies.map(o => s""""${o}"""").mkString(", ")}
        ]
      
      }
      
    }"""

  }

}

