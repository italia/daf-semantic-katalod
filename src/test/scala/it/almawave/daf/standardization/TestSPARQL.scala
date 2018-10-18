package it.almawave.daf.standardization

import java.net.URL
import java.nio.file.Paths

import it.almawave.linkeddata.kb.catalog.models.Hierarchy
import it.almawave.linkeddata.kb.catalog.{SPARQL, VocabularyBox}
import it.almawave.linkeddata.kb.utils.JSONHelper

import scala.collection.mutable.ListBuffer
import scala.io.Source

object TestSPARQL {


  def main(args: Array[String]): Unit = {

    val fileRdf = Paths.get("C:/Users/a.mauro/IdeaProjects/katalod/ontologie-vocabolari-controllati/VocabolariControllati/licences/licences.rdf").toString
    val fileQuerySparql = Paths.get("C:/Users/a.mauro/IdeaProjects/katalod/conf/query/skos/hierarchyOneLevel.sparql").toFile
    val skos_url = new URL(s"file:///$fileRdf")
    val lines: List[String] = Source.fromFile(fileQuerySparql).getLines.toList

    val voca = VocabularyBox.parse(skos_url)
    voca.start()

    val concepts = SPARQL(voca.repo).query( lines.toStream.mkString )
//    var hierarchy_broader = Hierarchy(null,null,null,null,null)
    var hierarchy_broader = ListBuffer[ListBuffer[Hierarchy]]()

    val list_hierarchy_broader = ListBuffer[Hierarchy]()

    /*concepts.toList.foreach { concept =>
      println(concept)
    }*/

    if(true) {

      concepts.toList
          .foreach { item => if(!item.contains("parent_uri")) {

                println("PADRE: \n" + item)
                // recupera le mappa contenente il primo livello gerarchico
                hierarchy_broader += read_concepts(scala.collection.mutable.Map(item.toSeq: _*), concepts)
//                println(JSONHelper.writeToString(hierarchy_broader))
            }
          }


    }//IF

    voca.stop()
    println(JSONHelper.writeToString(hierarchy_broader))
  }

  def read_concepts(key_item : scala.collection.mutable.Map[String,Any],
            _concepts : Seq[Map[String,Any]] ) : ListBuffer[Hierarchy] = {

      var tmp_hierarchy_child = ListBuffer[Hierarchy]()
      val hierarchy_child = ListBuffer[Hierarchy]()
//      val hierarchy = Hierarchy(key_item.get("codice").get.toString, key_item.get("label").get.toString, key_item.get("uri").get.toString, key_item.getOrElse("parent_uri", "").toString, new ListBuffer[Hierarchy]())
      hierarchy_child += convert_mutable_map_to_model(key_item)
//      var hierarchy = Hierarchy(null,null,null,null,null)
      hierarchy_child.copyToBuffer(tmp_hierarchy_child) //+= convert_mutable_map_to_model(key_item)
//    for (e <- tmp_hierarchy_child if(item.contains("parent_uri") & item.get("parent_uri").equals(ss.get("uri"))) yield e

      var children = true
      while(children) {

        //lista di collection temporanea
        tmp_hierarchy_child
          .toList.foreach { item_hierarchy =>

//          hierarchy = Hierarchy(item_hierarchy.codice, item_hierarchy.label, item_hierarchy.uri, item_hierarchy.parent_uri, ListBuffer[Hierarchy]())
  //          var tmp_hierarchy_child_sub = ListBuffer[scala.collection.mutable.Map[String,Any]]()
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
            if(tmp_hierarchy_child_sub != null && tmp_hierarchy_child_sub.size > 0) {

              tmp_hierarchy_child.clear()
              tmp_hierarchy_child_sub.copyToBuffer(tmp_hierarchy_child)
    //          hierarchy = Hierarchy(key_item.get("codice").get.toString, key_item.get("label").get.toString, key_item.get("uri").get.toString, key_item.getOrElse("parent_uri", "").toString, tmp_hierarchy_child_sub)

//              hierarchy = Hierarchy(item_hierarchy.codice, item_hierarchy.label, item_hierarchy.uri, item_hierarchy.parent_uri, tmp_hierarchy_child_sub)

//                hierarchy_child += hierarchy
//                addBroaderToList(hierarchy_child, tmp_hierarchy_child_sub)

              addBroaderToList(item_hierarchy, tmp_hierarchy_child_sub)

            }else
              children = false
          }
        }//while

//    println(JSONHelper.writeToString(hierarchy_child))
    hierarchy_child
  }

  def convert_mutable_map_to_model(item : scala.collection.mutable.Map[String,Any]) : Hierarchy = {

    val _hierarchy = Hierarchy(item.get("codice").get.toString, item.get("label").get.toString, item.get("uri").get.toString, item.getOrElse("parent_uri", "").toString, new ListBuffer[Hierarchy]())

    _hierarchy
  }

//  def addBroaderToList(listIn: ListBuffer[Hierarchy], listMem: ListBuffer[Hierarchy]) : ListBuffer[Hierarchy] = {
  def addBroaderToList(item_hierarchy: Hierarchy, listMem: ListBuffer[Hierarchy]) : Hierarchy = {

    if(listMem.toList.head.parent_uri.equals(item_hierarchy.uri))
//    listMem.toList.head{ item => if(item_hierarchy.uri.equals(item.parent_uri)) {
        listMem.copyToBuffer(item_hierarchy.children)
//      }
//    }
    item_hierarchy
  }

}
