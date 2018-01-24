package it.almawave.kb.http.endpoints

import javax.inject.Singleton
import javax.ws.rs.Path
import org.slf4j.LoggerFactory
import it.almawave.linkeddata.kb.catalog.ResourcesLoader

@Singleton
@Path("conf:api-catalog-config")
class CatalogService {

  private val logger = LoggerFactory.getLogger(this.getClass)
  private val USE_CACHE = false

  // TODO: externalize configurations
  var loader = ResourcesLoader("./conf/catalog.conf")

  // we pre-load all the metadata in memory at first request, 
  // so next requests will be more efficient
  val _vocabularies = loader.fetchVocabularies(USE_CACHE)
  val _ontologies = loader.fetchOntologies(USE_CACHE)

  logger.info(s"loaded ${_vocabularies.size} vocabularies")
  logger.info(s"loaded ${_ontologies.size} ontologies")

  def ontologies() = _ontologies
  def vocabularies() = _vocabularies

}
