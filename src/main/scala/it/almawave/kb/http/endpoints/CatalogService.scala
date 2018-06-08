package it.almawave.kb.http.endpoints

import javax.inject.Singleton
import javax.ws.rs.Path
import org.slf4j.LoggerFactory
import it.almawave.kb.http.models.OntologyMetaModel
import com.typesafe.config.ConfigFactory
import java.nio.file.Paths
import it.almawave.linkeddata.kb.catalog.CatalogBox
import it.almawave.linkeddata.kb.utils.JSONHelper

@Singleton
@Path("conf://api-catalog-config")
class CatalogService {

  private val logger = LoggerFactory.getLogger(this.getClass)

  /*
   * TODO: add parameters to endpoints
   * TODO: create an instance for each lang
   */
  val LANG = "it"

  // first version!
  // we pre-load all the metadata in memory at first request,
  // so next requests will be more efficient
  //  var loader = ResourcesLoader("./conf/catalog.conf")
  //  val _vocabularies = loader.fetchVocabularies(USE_CACHE)
  //  val _ontologies = loader.fetchOntologies(USE_CACHE)

  //  REFACTORIZATION: NEW VERSION (v0.0.2)
  // we still assume a fixed `conf/catalog.conf` configuration file, here... this mau change in later versions
  val conf = ConfigFactory.parseFile(Paths.get("./conf/catalog.conf").normalize().toFile())
  val catalog = new CatalogBox(conf)
  catalog.start()

  // references to pre-loaded metadata for ontologies and vocabularies
  val _ontologies = catalog.ontologies.map(_.meta)
  val _vocabularies = catalog.vocabularies.map(_.meta)

  val _ontologies_aligns = catalog.ontologies_aligns.map(_.meta)

  logger.info(s"loaded ${_vocabularies.size} vocabularies")
  logger.info(s"loaded ${_ontologies.size} ontologies")
  logger.info(s"loaded ${_ontologies_aligns.size} ontologies_aligns")

  def ontologies() = _ontologies

  def ontologies_aligns() = _ontologies_aligns

  def vocabularies() = _vocabularies

}
