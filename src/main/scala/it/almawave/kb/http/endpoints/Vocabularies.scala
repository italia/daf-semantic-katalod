package it.almawave.kb.http.endpoints

import java.net.URL
import java.nio.file.Paths

import javax.inject.Inject
import javax.ws.rs._
import io.swagger.annotations.{ Api, ApiOperation, ApiParam, Tag }
import javax.ws.rs.core.{ MediaType, Response }
import com.sun.net.httpserver.Authenticator.Success
import com.typesafe.config.ConfigFactory
import it.almawave.linkeddata.kb.catalog.models
import it.almawave.linkeddata.kb.catalog.models.Hierarchy
//import it.almawave.kb.http.models.Hierarchy
import it.almawave.linkeddata.kb.catalog.models.VocabularyMeta
import it.almawave.linkeddata.kb.catalog.{ SPARQL, VocabularyBox }
import it.almawave.linkeddata.kb.utils.JSONHelper
import org.slf4j.LoggerFactory

import scala.collection.mutable.ListBuffer
import scala.io.Source

@Api(tags = Array("catalog"))
@Path("/vocabularies")
class Vocabularies {

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

  //TODO SERVIZIO DA CANCELLARE
  @GET
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Tag(name = "catalog, vocabularies")
  @ApiOperation(nickname = "vocabularyDetails", value = "details of a vocabulary")
  @Path("/{vocabularyID}")
  def details(@PathParam("vocabularyID") id: String) = {
    logger.debug(s"getting the details for vocabulary with id: ${id}")
    val vMeta: VocabularyMeta = loader.vocabularies()
      .filter { item => item.id.equalsIgnoreCase(id) }
      .headOption.get

    HierarchyUtility.setHierarchy(vMeta)

    Response.ok(vMeta).build()

  }

  @GET
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Tag(name = "catalog, vocabularies")
  @ApiOperation(nickname = "vocabularyFlat", value = "details of a vocabulary custom")
  @Path("/flat/{vocabularyID}")
  def detailsCustom(
    @PathParam("vocabularyID")@DefaultValue("licences") vocabularyID:     String,
    @ApiParam(required = false)@QueryParam("lang")@DefaultValue("it") lang:String) = {

    logger.debug(s"getting the details for vocabulary bis with id: ${vocabularyID} and lang: $lang")
    val vbox = loader.catalog.getVocabularyByID(vocabularyID).get
    val vboxid = vbox.meta.source
    logger.debug("vboxid: " + vboxid)

    val fileRdf = Paths.get(vboxid.toURI).toString
    val skos_url = new URL(s"file:///$fileRdf")

    val conf = ConfigFactory.parseFile(Paths.get("./conf/catalog.conf").normalize().toFile()).getConfig("daf.vocabularies")
    val config_dir = Paths.get(conf.root().origin().filename()).toFile().getParent
    val default_query_hierarchy: java.nio.file.Path = Paths.get(config_dir, conf.getString("query.hierarchy")).normalize().toAbsolutePath()
    val lines: List[String] = Source.fromFile(default_query_hierarchy.toUri).getLines.toList

    val voca = VocabularyBox.parse(skos_url)
    voca.start()

    val concepts = SPARQL(voca.repo).query(lines.toStream.mkString)

    voca.stop()

    Response.ok(concepts).build()
  }

  @GET
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Tag(name = "catalog, vocabularies")
  @ApiOperation(nickname = "vocabularyHierarchy", value = "details of a vocabulary custombis")
  @Path("/hierarchy/{vocabularyID}")
  def detailsCustomBis(
    @PathParam("vocabularyID")@DefaultValue("licences") vocabularyID:     String,
    @ApiParam(required = false)@QueryParam("lang")@DefaultValue("it") lang:String) = {

    logger.debug(s"getting the details for vocabulary bis with id: ${vocabularyID} and lang: $lang")

    val vbox = loader.catalog.getVocabularyByID(vocabularyID).get
    val vboxid = vbox.meta.source
    logger.debug("vboxid: " + vboxid)

    val fileRdf = Paths.get(vboxid.toURI).toString
    val skos_url = new URL(s"file:///$fileRdf")

    val conf = ConfigFactory.parseFile(Paths.get("./conf/catalog.conf").normalize().toFile()).getConfig("daf.vocabularies")
    val config_dir = Paths.get(conf.root().origin().filename()).toFile().getParent
    val default_query_hierarchy: java.nio.file.Path = Paths.get(config_dir, conf.getString("query.hierarchy")).normalize().toAbsolutePath()
    val lines: List[String] = Source.fromFile(default_query_hierarchy.toUri).getLines.toList

    val voca = VocabularyBox.parse(skos_url)

    voca.start()

    val concepts = SPARQL(vbox.repo).query(lines.toStream.mkString)
    val hierarchy_broader = ListBuffer[Hierarchy]()

    if (true) {

      concepts.toList
        .foreach { item =>
          if (!item.contains("parent_uri")) {
            println("broader: \n" + item)
            val map_item = scala.collection.mutable.Map(item.toSeq: _*)
            HierarchyUtility.read_concepts(map_item, concepts, hierarchy_broader)
          }
        }
    }

    voca.stop()
    Response.ok(hierarchy_broader).build()
  }

  object HierarchyUtility {

    def setHierarchy(vMeta: VocabularyMeta) = {

      logger.debug(s"getting the details for vocabulary bis with id: ${vMeta.id} and lang: ${vMeta.langs.headOption.getOrElse("")}")

      val vbox = loader.catalog.getVocabularyByID(vMeta.id).get
      val vboxid = vbox.meta.source
      logger.debug("vboxid: " + vboxid)

      val fileRdf = Paths.get(vboxid.toURI).toString
      val skos_url = new URL(s"file:///$fileRdf")

      val conf = ConfigFactory.parseFile(Paths.get("./conf/catalog.conf").normalize().toFile()).getConfig("daf.vocabularies")
      val config_dir = Paths.get(conf.root().origin().filename()).toFile().getParent
      val default_query_hierarchy: java.nio.file.Path = Paths.get(config_dir, conf.getString("query.hierarchy")).normalize().toAbsolutePath()
      val lines: List[String] = Source.fromFile(default_query_hierarchy.toUri).getLines.toList

      val voca = VocabularyBox.parse(skos_url)

      voca.start()

      val concepts = SPARQL(vbox.repo).query(lines.toStream.mkString)
      vMeta.hierarchy.clear()
      val hierarchy_broader: ListBuffer[Hierarchy] = vMeta.hierarchy

      if (true) {

        concepts.toList
          .foreach { item =>
            if (!item.contains("parent_uri")) {
              println("broader: \n" + item)
              HierarchyUtility.read_concepts(scala.collection.mutable.Map(item.toSeq: _*), concepts, hierarchy_broader)
            }
          }
      }

      voca.stop()
      hierarchy_broader
    }

    def read_concepts(
      key_item:  scala.collection.mutable.Map[String, Any],
      _concepts: Seq[Map[String, Any]], hierarchy_child: ListBuffer[Hierarchy]): ListBuffer[Hierarchy] = {

      val tmp_hierarchy_child = ListBuffer[Hierarchy]()
      hierarchy_child += convert_mutable_map_to_model(key_item)
      tmp_hierarchy_child += hierarchy_child.last
      var children = true

      while (children) {

        //lista di collection temporanea
        for(item_hierarchy <- tmp_hierarchy_child) {
            var tmp_hierarchy_child_sub = ListBuffer[Hierarchy]()
            _concepts
              .toList.foreach {
                item =>
                  if (item.contains("parent_uri") & item.getOrElse("parent_uri", "").toString.equals(item_hierarchy.uri)) {

                    tmp_hierarchy_child_sub += convert_mutable_map_to_model(scala.collection.mutable.Map(item.toSeq: _*))

                    children = true
                  }
              }

            // cocludendo la lettura di tutti i concetti aggiungo alla lista tmp gli elementi che hanno soddifatto la condizione
            if (tmp_hierarchy_child_sub != null && tmp_hierarchy_child_sub.size > 0) {
              tmp_hierarchy_child == ListBuffer[Hierarchy]()
              tmp_hierarchy_child_sub.copyToBuffer(tmp_hierarchy_child)
              addBroaderToList(item_hierarchy, tmp_hierarchy_child_sub)
            } else
              children = false
          }
      } //while

      hierarchy_child
    }

    def convert_mutable_map_to_model(item: scala.collection.mutable.Map[String, Any]): Hierarchy = {

      val _hierarchy = Hierarchy(item.get("codice").get.toString, item.get("label").get.toString,
        item.get("uri").get.toString, item.getOrElse("parent_uri", "").toString, new ListBuffer[Hierarchy]())
      _hierarchy
    }

    def addBroaderToList(_item_hierarchy: Hierarchy, listMem: ListBuffer[Hierarchy]): Hierarchy = {

      if (listMem.toList.head.parent_uri.equals(_item_hierarchy.uri))
        listMem.copyToBuffer(_item_hierarchy.children)

      _item_hierarchy
    }

  }

}



