package it.almawave.kb

import java.net.URL
import java.nio.file.{Path, Paths}

import com.typesafe.config.ConfigFactory
import it.almawave.kb.http.endpoints.CatalogService
import it.almawave.linkeddata.kb.catalog.{SPARQL, VocabularyBox}
import javax.inject.Inject
import javax.ws.rs.core.Response
import org.slf4j.LoggerFactory




import scala.io.Source

object MainVerifyConcept extends App {

  private val logger = LoggerFactory.getLogger(this.getClass)


  var loader: CatalogService = new CatalogService

//  val vocabularyID = "territorial-classification"
  val vocabularyID = "licences"

//  val fileRdf = Paths.get("./ontologie-vocabolari-controllati/VocabolariControllati/classifications-for-public-services/authentication-type/channel/channel.rdf").toString

  val vbox = loader.catalog.getVocabularyByID(vocabularyID).get
  val vboxid = vbox.meta.source
  logger.debug("vboxid: " + vboxid)

  val fileRdf = Paths.get(vboxid.toURI).toString
  val conf = ConfigFactory.parseFile(Paths.get("./conf/catalog.conf").normalize().toFile()).getConfig("daf.vocabularies")

  val skos_url = new URL(s"file:///$fileRdf")

  val config_dir = Paths.get(conf.root().origin().filename()).toFile().getParent
//  val queries_dir = conf.getString("queries.dir")
  val query_hierarchy_checkSkos: Path = Paths.get(config_dir, conf.getString("query.checkSkos")).normalize().toAbsolutePath()
  val query_hierarchy: Path = Paths.get(config_dir, conf.getString("query.hierarchy")).normalize().toAbsolutePath()

  var lines: List[String] = Source.fromFile(query_hierarchy_checkSkos.toUri).getLines.toList

  println(lines)

  val voca = VocabularyBox.parse(skos_url)
  voca.start()

  var concepts = SPARQL(voca.repo).query( lines.toStream.mkString )
  if(concepts.isEmpty) {
    logger.info("concepts is empty")
    logger.info("concepts: " + concepts)
    logger.info("#################### ESEGUIRE QUERY SPARQL SPECIFICA ####################")

  }else {
    logger.info("#################### ESEGUIRE QUERY SPARQL SPECIFICA SKOS ####################")
    // sottoponiamo la query specifica
    lines = Source.fromFile(query_hierarchy.toUri).getLines.toList
    concepts = SPARQL(voca.repo).query( lines.toStream.mkString )
    if(concepts.isEmpty)
      logger.info("concepts is empty")


    val l = concepts.toList
    logger.info("concepts: " + l)
  }

//  Response.ok(concepts).build()

  voca.stop()

}
