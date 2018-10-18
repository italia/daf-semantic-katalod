package it.almawave.daf.standardization

import java.net.URL
import java.nio.file.Paths

import it.almawave.linkeddata.kb.catalog.models.Hierarchy
import it.almawave.linkeddata.kb.catalog.{OntologyBox, SPARQL, VocabularyBox}
import it.almawave.linkeddata.kb.utils.JSONHelper

import scala.collection.mutable.ListBuffer
import scala.io.Source

object TestSPARQL_Onto {


  def main(args: Array[String]): Unit = {

    val fileRdf = Paths.get("c:/Users/a.mauro/IdeaProjects/katalod/ontologie-vocabolari-controllati/Ontologie/ADMS/latest/ADMS-AP_IT.rdf").toString
    //val fileQuerySparql = Paths.get("C:/Users/a.mauro/IdeaProjects/katalod/conf/query/skos/hierarchyOneLevel.sparql").toFile
    val skos_url = new URL(s"file:///$fileRdf")


    println(s"\n\nextracting informations from\n${skos_url}\n...")

    val box = OntologyBox.parse(skos_url)
    box.start()
    println(box)

    val json = JSONHelper.writeToString(box.meta)
    println(json)

    box.stop()

  }

}
