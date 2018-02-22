package it.almawave.kb.http.endpoints

import it.almawave.daf.standardization.StandardizationProcess
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import io.swagger.annotations.ApiOperation
import javax.ws.rs.GET
import javax.ws.rs.Path
import io.swagger.annotations.Api
import javax.ws.rs.PathParam
import io.swagger.annotations.Tag

@Api(tags = Array("DAF", "vocabularies"))
@Path("/daf/standardization")
class DAFStandardization {

  val logger = LoggerFactory.getLogger(this.getClass)

  @Inject
  var catalog_service: CatalogService = null

  @GET
  @Produces(Array(MediaType.APPLICATION_JSON))
  @ApiOperation(nickname = "vocabulariesListForStandardization", value = "vocabulary dataset, prepared for standardization")
  @Path("/vocabularies")
  def allFlatVocabularies() = {

    logger.debug(s"getting the standardized (and de-normalized) form for all the vocabularies")

    // pre-load ALL the standardized vocabularies, for DAF
    catalog_service.daf_standardized_data.toList

  }

  @GET
  @Produces(Array(MediaType.APPLICATION_JSON))
  @ApiOperation(nickname = "vocabularyForStandardization", value = "vocabulary dataset, prepared for standardization")
  @Path("/vocabularies/{vocabularyID}")
  def flatVocabulary(@PathParam("vocabularyID") vocabularyID: String) = {

    logger.debug(s"getting the standardized (and de-normalized) form of the vocabulary ${vocabularyID}")

    catalog_service.daf_standardized_data.toStream
      .filter(_.vocabularyID.equals(vocabularyID.trim()))
      .toList
      .headOption.getOrElse(s"""{
        "msg": "cannot find a representation for ${vocabularyID}"
      }""")

  }

}