package it.almawave.daf.standardization

import java.net.URL
import java.nio.file.Paths

import it.almawave.linkeddata.kb.catalog.models.Hierarchy
import it.almawave.linkeddata.kb.catalog.{SPARQL, VocabularyBox}
import it.almawave.linkeddata.kb.utils.JSONHelper

import scala.collection.mutable.ListBuffer
import scala.io.Source

object TestSPARQL_2 {


  def main(args: Array[String]): Unit = {

//    val hres = SPARQL(vbox.repo).query("")
//    .map { tuple =>
//
//      val _id = tuple.getOrElse("uri", "").asInstanceOf[String]
//      val _notation = tuple.getOrElse("notation", "").asInstanceOf[String]
//
//      RESULT(_id, _notation)
//    }

//    println(JSONHelper.writeToString(hres))

//    val fileRdf = Paths.get("C:/Users/a.mauro/IdeaProjects/katalod/ontologie-vocabolari-controllati/VocabolariControllati/licences/licences.rdf").toString
    val fileRdf = Paths.get("C:/Users/a.mauro/IdeaProjects/katalod/ontologie-vocabolari-controllati/VocabolariControllati/classifications-for-public-services/authentication-type/channel/channel.rdf").toString
    val fileQuerySparql = Paths.get("C:/Users/a.mauro/IdeaProjects/katalod/conf/query/skos/hierarchyOneLevel.sparql").toFile

    val skos_url = new URL(s"file:///$fileRdf")

    val lines: List[String] = Source.fromFile(fileQuerySparql).getLines.toList

    val voca = VocabularyBox.parse(skos_url)
    voca.start()

    val concepts = SPARQL(voca.repo).query( lines.toStream.mkString )
//    var hierarchy_broader = Hierarchy(null,null,null,null,null)
    var hierarchy_broader = ListBuffer[ListBuffer[Hierarchy]]()

    val list_hierarchy_broader = ListBuffer[Hierarchy]()


//    conceptsbis += conceptsbis2


//    concepts.toList.foreach { concept =>
//
//            println(concept)
//
//    }

    if(true) {

      concepts.toList
//        .map { item => item.getOrElse("parent_uri", pippo()).toString() }
//        .filter { item => item.trim().equalsIgnoreCase("") }
          .foreach { item => if(!item.contains("parent_uri")) {

                println("PADRE: \n" + item)
                // recupera le mappa contenente il primo livello gerarchico
//                val appo = ListBuffer[ListBuffer[scala.collection.mutable.Map[String,Any]]]()
//                conceptsbis2 += scala.collection.mutable.Map(item.toSeq: _*)
//                appo += conceptsbis2
//                conceptsbis += appo
//                concepts.toList.foreach { item_child =>
//                if (d.contains("parent_uri") & d.get("parent_uri").equals(item.get("uri"))) {
//                conceptsbis += pippo(scala.collection.mutable.Map(item.toSeq: _*), concepts)

//                  var hierarchy_broader = Hierarchy(item.get("codice").get.toString, item.get("label").get.toString,
//                                            item.get("uri").get.toString, item.getOrElse("parent_uri", "").toString,
//                                            read_concepts(scala.collection.mutable.Map(item.toSeq: _*), concepts))

//                  list_hierarchy_broader += hierarchy_broader
//                  list_hierarchy_broader == read_concepts(scala.collection.mutable.Map(item.toSeq: _*), concepts)
                  hierarchy_broader += read_concepts(scala.collection.mutable.Map(item.toSeq: _*), concepts)


        //                }
//              }

            }

          }

    /*val concepts = SPARQL(voca.repo).query("""

    PREFIX skos: <http://www.w3.org/2004/02/skos/core#>

    SELECT *

      WHERE {

          ?uri a skos:Concept .

          OPTIONAL{ ?uri skos:broader ?parent_uri . }

          ?uri skos:notation ?codice .

          ?uri skos:prefLabel ?label .

          FILTER(LANG(?label) = 'it')

      }

      ORDER BY ?uri

  """).toList*/
    //    .map { item => item.getOrElse("concept", "").toString() }
    //    .filter { item => item.trim().equalsIgnoreCase("") }

//    println("SKOS concepts:")

//    concepts.foreach { concept =>
//
//      println(concept)
//
//    }

//    println("####################################################################")
    println(JSONHelper.writeToString(hierarchy_broader))

//    JSONHelper.writeToString(concepts)

    voca.stop()

    }//IF

  }

  def read_concepts(key_item : scala.collection.mutable.Map[String,Any],
            _concepts : Seq[Map[String,Any]] ) : ListBuffer[Hierarchy] = {

//    val _result = scala.collection.mutable.Map(_item.toSeq: _*)
//      var tmp_hierarchy_child = ListBuffer[scala.collection.mutable.Map[String,Any]]()
      var tmp_hierarchy_child = ListBuffer[Hierarchy]()
      val hierarchy_child = ListBuffer[Hierarchy]()

//    val hierarchy = Hierarchy(key_item.get("codice").toString, key_item.get("label").toString, key_item.get("uri").toString, key_item.get("parent_uri").toString, new ListBuffer[Hierarchy]())
      var hierarchy = Hierarchy(null,null,null,null,null)
      tmp_hierarchy_child += convert_mutable_map_to_model(key_item)


//    for (e <- tmp_hierarchy_child if(item.contains("parent_uri") & item.get("parent_uri").equals(ss.get("uri"))) yield e

    var children = true
    while(children) {

      //lista di collection temporanea
      tmp_hierarchy_child
        .toList.foreach { item_hierarchy =>

//          var tmp_hierarchy_child_sub = ListBuffer[scala.collection.mutable.Map[String,Any]]()
          var tmp_hierarchy_child_sub = ListBuffer[Hierarchy]()
          _concepts
            .toList.foreach {
            item =>
            if (item.contains("parent_uri") & item.getOrElse("parent_uri", "").toString.equals(item_hierarchy.uri)) {

//              tmp_hierarchy_child_sub += scala.collection.mutable.Map(item.toSeq: _*)
              tmp_hierarchy_child_sub += convert_mutable_map_to_model(scala.collection.mutable.Map(item.toSeq: _*))

              //              hierarchy_child += tmp_hierarchy_child_sub

              children = true
            }
//            else if(hierarchy_child != null && hierarchy_child.size > 0){
//
//              println("\n   " + item)
//
//            }
//            else
//              children = false
    //
          }

        // cocludendo la lettura di tutti i concetti aggiungo alla lista tmp gli elementi che hanno soddifatto la condizione
        if(tmp_hierarchy_child_sub != null && tmp_hierarchy_child_sub.size > 0) {

          tmp_hierarchy_child.clear()
          tmp_hierarchy_child_sub.copyToBuffer(tmp_hierarchy_child)
//          hierarchy = Hierarchy(key_item.get("codice").get.toString, key_item.get("label").get.toString, key_item.get("uri").get.toString, key_item.getOrElse("parent_uri", "").toString, tmp_hierarchy_child_sub)
          hierarchy = Hierarchy(item_hierarchy.codice, item_hierarchy.label, item_hierarchy.uri, item_hierarchy.parent_uri, tmp_hierarchy_child_sub)

          // Da rivedere con la nuova logica ad oggetti
//          tmp_hierarchy_child = ListBuffer[scala.collection.mutable.Map[String,Any]]()
//          tmp_hierarchy_child_sub.copyToBuffer(tmp_hierarchy_child)
//          tmp_hierarchy_child_sub.clear()

          hierarchy_child += hierarchy

        }else
          children = false


      }


      }

//    println(JSONHelper.writeToString(hierarchy_child))
    hierarchy_child
  }

  def convert_mutable_map_to_model(item : scala.collection.mutable.Map[String,Any]) : Hierarchy = {

    val _hierarchy = Hierarchy(item.get("codice").get.toString, item.get("label").get.toString, item.get("uri").get.toString, item.getOrElse("parent_url", "").toString, new ListBuffer[Hierarchy]())

    _hierarchy
  }

}
