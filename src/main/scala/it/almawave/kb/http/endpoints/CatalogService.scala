package it.almawave.kb.http.endpoints

import javax.inject.Singleton
import javax.ws.rs.Path
import org.slf4j.LoggerFactory
import it.almawave.linkeddata.kb.catalog.ResourcesLoader
import it.almawave.kb.http.models.OntologyMetaModel
import com.typesafe.config.ConfigFactory
import java.nio.file.Paths
import it.almawave.linkeddata.kb.catalog.ResourcesLoader
import it.almawave.linkeddata.kb.catalog.CatalogBox

@Singleton
@Path("conf:api-catalog-config")
class CatalogService {

  private val logger = LoggerFactory.getLogger(this.getClass)

  // load some defualt configuration from file
  //  val _conf = ConfigFactory
  //    .parseFileAnySyntax(Paths.get("./conf/catalog.conf").normalize().toFile())
  //    .resolve()

  // decide weather to use or not the RDF files copied locally
  //  private val USE_CACHE = if (_conf.hasPath("use_cache")) _conf.getBoolean("use_cache") else false

  private val USE_CACHE = true

  // TODO: externalize configurations

  // first version!
  // we pre-load all the metadata in memory at first request,
  // so next requests will be more efficient
  var loader = ResourcesLoader("./conf/catalog.conf")
  val _vocabularies = loader.fetchVocabularies(USE_CACHE)
  val _ontologies = loader.fetchOntologies(USE_CACHE)

  //  REFACTORIZATION: NEW VERSION (v0.0.2)
  //  val conf = ConfigFactory.parseFile(Paths.get("./conf/catalog.conf").normalize().toFile())
  //  val catalog = new CatalogBox(conf)
  //  catalog.start()
  //  val _vocabularies = catalog.vocabularies
  //  val _ontologies = catalog.ontologies

  logger.info(s"loaded ${_vocabularies.size} vocabularies")
  logger.info(s"loaded ${_ontologies.size} ontologies")

  def ontologies() = _ontologies

  def vocabularies() = _vocabularies

  /* CHECK serializers for Catalog! SEE: case classes as models
  No serializer found for class org.eclipse.rdf4j.model.impl.SimpleValueFactory
  	and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS)
  	(through reference chain:
  		scala.collection.convert.Wrappers$IterableWrapper[0]->
    		it.almawave.linkeddata.kb.catalog.VocabularyBox["repo"]->
    		it.almawave.linkeddata.kb.file.RDFFileRepository["sail"]->
    		it.almawave.linkeddata.kb.file.RDFFileSail["vf"]
    )
	*/
}
