package it.almawave.kb.http.endpoints

import java.net.URL
import java.nio.file.Paths
import javax.inject.Inject
import javax.ws.rs._

import io.swagger.annotations.{ Api, ApiOperation, ApiParam, Tag }
import javax.ws.rs.core.{ MediaType, Response }

import com.sun.net.httpserver.Authenticator.Success
import it.almawave.kb.http.models.Hierarchy
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
    loader.vocabularies()
      .filter { item => item.id.equalsIgnoreCase(id) }
      .headOption
  }

  @GET
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Tag(name = "catalog, vocabularies")
  @ApiOperation(nickname = "vocabularyFlat", value = "details of a vocabulary - flat list")
  @Path("/flat/{vocabularyID}")
  def detailsFlat(
    @PathParam("vocabularyID")@DefaultValue("licences") vocabularyID:     String,
    @ApiParam(required = false)@QueryParam("lang")@DefaultValue("it") lang:String) = {

    logger.debug(s"getting the details (flat list) for vocabulary bis with id: ${vocabularyID} and lang: $lang")
    val vboxid = loader.catalog.getVocabularyByID(vocabularyID).get
    //    logger.debug("vboxid: " + vboxid)
    //    logger.debug("vboxid source: " + vboxid.meta.source)

    val fileRdf = Paths.get(s"./ontologie-vocabolari-controllati/VocabolariControllati/${vocabularyID}/${vocabularyID}.rdf").toAbsolutePath().toString
    val fileQuerySparql = Paths.get("./conf/query/skos/hierarchyOneLevel.sparql").toAbsolutePath().toFile

    val skos_url = new URL(s"file:///$fileRdf")

    val lines = Source.fromFile(fileQuerySparql).getLines.toList

    val vbox = loader.catalog.getVocabularyByID(vocabularyID).get
    // val voca = VocabularyBox.parse(skos_url)
    vbox.start()

    val concepts = SPARQL(vbox.repo).query(lines.toStream.mkString)

    vbox.stop()

    Response.ok(concepts).build()
  }

  @GET
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Tag(name = "catalog, vocabularies")
  @ApiOperation(nickname = "vocabularyHierarchy", value = "details of a vocabulary - hierarchy")
  @Path("/hierarchy/{vocabularyID}")
  def detailsHierarchy(
    @PathParam("vocabularyID")@DefaultValue("licences") vocabularyID:     String,
    @ApiParam(required = false)@QueryParam("lang")@DefaultValue("it") lang:String) = {

    logger.debug(s"getting the details (hierarchy) for vocabulary bis with id: ${vocabularyID} and lang: $lang")

    val vbox = loader.catalog.getVocabularyByID(vocabularyID).get
    val vboxid = vbox.meta.source
    //    logger.debug("vboxid: " + vboxid)

    val fileRdf = Paths.get(vboxid.toURI).toString
    val fileQuerySparql = Paths.get("./conf/query/skos/hierarchyOneLevel.sparql").toAbsolutePath().toFile
    val skos_url = new URL(s"file:///$fileRdf")
    val lines = Source.fromFile(fileQuerySparql).getLines.toList
    val voca = VocabularyBox.parse(skos_url)

    voca.start()

    val concepts = SPARQL(vbox.repo).query(lines.toStream.mkString)
    val hierarchy_broader = ListBuffer[Hierarchy]()

    //    concepts.toList.foreach { concept =>
    //    println(concept)
    //    }
    if (true) {

      concepts.toList
        .foreach { item =>
          if (!item.contains("parent_uri")) {
            println("PADRE: \n" + item)
            //            hierarchy_broader += read_concepts(scala.collection.mutable.Map(item.toSeq: _*), concepts, hierarchy_broader)
            HierarchyUtility.read_concepts(scala.collection.mutable.Map(item.toSeq: _*), concepts, hierarchy_broader)
          }
        }
    }

    voca.stop()
    Response.ok(hierarchy_broader).build()
  }

  object HierarchyUtility {

    def read_concepts(
      key_item:  scala.collection.mutable.Map[String, Any],
      _concepts: Seq[Map[String, Any]], hierarchy_child: ListBuffer[Hierarchy]): ListBuffer[Hierarchy] = {

      val tmp_hierarchy_child = ListBuffer[Hierarchy]()
      hierarchy_child += convert_mutable_map_to_model(key_item)
      tmp_hierarchy_child += hierarchy_child.last
      var children = true

      while (children) {

        //lista di collection temporanea
        tmp_hierarchy_child
          .toList.foreach { item_hierarchy =>
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
              tmp_hierarchy_child.clear()
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



