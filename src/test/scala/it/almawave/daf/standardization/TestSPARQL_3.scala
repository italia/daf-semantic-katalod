package it.almawave.daf.standardization

import java.net.URL
import java.nio.file.Paths

import it.almawave.linkeddata.kb.catalog.models.URIWithLabel
import it.almawave.linkeddata.kb.catalog.{SPARQL, VocabularyBox}

object TestSPARQL_3 {

  var voca: VocabularyBox = _

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
    val fileRdf = Paths.get("C:/Users/a.mauro/IdeaProjects/katalod/ontologie-vocabolari-controllati/VocabolariControllati/classifications-for-accommodation-facilities/accommodation-star-rating/accommodation-star-rating.rdf").toString
//    val fileQuerySparql = Paths.get("C:/Users/a.mauro/IdeaProjects/katalod/conf/query/skos/hierarchyOneLevel.sparql").toFile

    val skos_url = new URL(s"file:///$fileRdf")

//    val lines: List[String] = Source.fromFile(fileQuerySparql).getLines.toList

    voca = VocabularyBox.parse(skos_url)
    voca.start()

    val concepts = SPARQL(voca.repo).query("""

        PREFIX dct: <http://purl.org/dc/terms/>
        PREFIX dcatapit: <http://dati.gov.it/onto/dcatapit#>
        PREFIX foaf: <http://xmlns.com/foaf/0.1/>
        SELECT DISTINCT ?agent_uri
        #FROM <https://w3id.org/italia/controlled-vocabulary/classifications-for-accommodation-facilities/accommodation-star-rating.>
        WHERE {
          ?agent_uri a dcatapit:Agent .
          #?agent_uri foaf:name ?name .
          #BIND(LANG(?name) AS ?name_lang)
        }

        """)
//        .map { item => println(item) }
    //    .filter { item => item.trim().equalsIgnoreCase("") }


    //Funziona
    /*val tags: Seq[URIWithLabel] = this.parse_dcat_keywords()
    val tags2: Seq[URIWithLabel] = this.parse_dcat_keywords()
    tags.union(tags2)*/

    var tags_container: Seq[URIWithLabel] = Seq[URIWithLabel]()
    var tags_container_tmp: Seq[URIWithLabel] = Seq[URIWithLabel]()
    concepts.foreach { concept =>

      val context = scala.collection.mutable.Map(concept.toSeq: _*).get("agent_uri").get.toString
      println(context)
      tags_container_tmp = this.parse_creators(context)
      //chiamare la funzione di recupero creator per ogni singola uri
//      if(!tags_container.isEmpty)
      tags_container = tags_container_tmp.union(tags_container)



//
//      val concepts2 = SPARQL(voca.repo).query(s"""
//
//        PREFIX dct: <http://purl.org/dc/terms/>
//        PREFIX dcatapit: <http://dati.gov.it/onto/dcatapit#>
//        PREFIX foaf: <http://xmlns.com/foaf/0.1/>
//        SELECT *
//        WHERE {
//          ?agent_uri a dcatapit:Agent .
//          ?agent_uri foaf:name ?name .
//          FILTER(?agent_uri=<$context>) .
//          BIND(LANG(?name) AS ?name_lang)
//        }
//
//        """).toList
//
//      concepts2.foreach { item =>
//
//        println("uri: " + scala.collection.mutable.Map(item.toSeq: _*).get("agent_uri").get)
//        println("name: " + scala.collection.mutable.Map(item.toSeq: _*).get("name").get)
//        println("lang: " + scala.collection.mutable.Map(item.toSeq: _*).get("name_lang").get)
//        println()
//        println("###############################################################")
//        println()
//      }

    }

    println(tags_container.toList)

//    println("####################################################################")
//    println(JSONHelper.writeToString(hierarchy_broader))

//    JSONHelper.writeToString(concepts)



    voca.stop()

  }

  def parse_creators(context : String): Seq[URIWithLabel] = {
//  def parse_creators() = {
    //    SPARQL(voca.repo).query("""
    //        PREFIX dct: <http://purl.org/dc/terms/>
    //        PREFIX dcatapit: <http://dati.gov.it/onto/dcatapit#>
    //        SELECT *
    //        FROM <test://accommodation-star-rating>
    //        WHERE {
    //          ?agent_uri a dcatapit:Agent .
    //          ?agent_uri foaf:name ?name .
    //          BIND(LANG(?name) AS ?name_lang)
    //        }
    //      """)
    //      // REFACTORIZATION: .map(_.getOrElse("license_uri", "").asInstanceOf[String])
    //      .map(_.getOrElse("license_uri", "").toString())
    //      .filterNot(_.equalsIgnoreCase(""))
    //      .map { item =>
    //        val uri = item
    //        val label = URIHelper.extractLabelFromURI(uri)
    //        val lang = ""
    //        URIWithLabel(label, uri, lang)
    //      }


//    val concepts = SPARQL(voca.repo).query(
//      """
//
//        PREFIX dct: <http://purl.org/dc/terms/>
//        PREFIX dcatapit: <http://dati.gov.it/onto/dcatapit#>
//        PREFIX foaf: <http://xmlns.com/foaf/0.1/>
//        SELECT DISTINCT ?agent_uri
//        #FROM <https://w3id.org/italia/controlled-vocabulary/classifications-for-accommodation-facilities/accommodation-star-rating.>
//        WHERE {
//          ?agent_uri a dcatapit:Agent .
//          #?agent_uri foaf:name ?name .
//          #BIND(LANG(?name) AS ?name_lang)
//        }
//
//        """).toList
//    //    .map { item => item.getOrElse("concept", "").toString() }
//    //    .filter { item => item.trim().equalsIgnoreCase("") }
//
//    println("SKOS concepts:")
//
//    concepts.foreach { concept =>
//
//      //      println(scala.collection.mutable.Map(concept.toSeq: _*).get("agent_uri").get)
//      val context = scala.collection.mutable.Map(concept.toSeq: _*).get("agent_uri").get

      //        println(context)


      SPARQL(voca.repo).query(
        s"""

        PREFIX dct: <http://purl.org/dc/terms/>
        PREFIX dcatapit: <http://dati.gov.it/onto/dcatapit#>
        PREFIX foaf: <http://xmlns.com/foaf/0.1/>
        SELECT *
        WHERE {
          ?agent_uri a dcatapit:Agent .
          ?agent_uri foaf:name ?name .
          FILTER(?agent_uri=<$context>) .
          BIND(LANG(?name) AS ?name_lang)
        }

        """).map { item =>
        val uri = scala.collection.mutable.Map(item.toSeq: _*).get("agent_uri").get.toString
        val label = scala.collection.mutable.Map(item.toSeq: _*).get("name").get.toString
        val lang = scala.collection.mutable.Map(item.toSeq: _*).get("name_lang").get.toString
        URIWithLabel(label, uri, lang)

      }

      //     concepts2.foreach { item =>
      //
      //        println("uri: " + scala.collection.mutable.Map(item.toSeq: _*).get("agent_uri").get)
      //        println("name: " + scala.collection.mutable.Map(item.toSeq: _*).get("name").get)
      //        println("lang: " + scala.collection.mutable.Map(item.toSeq: _*).get("name_lang").get)
      //        println()
      //        println("###############################################################")
      //        println()
//    }

  }

  def parse_dc_subjects() = {
    val concept = SPARQL(voca.repo).query("""
      PREFIX dct: <http://purl.org/dc/terms/>
      PREFIX skos: <http://www.w3.org/2004/02/skos/core#>
      SELECT DISTINCT ?subject
      WHERE { ?uri a skos:ConceptScheme . ?uri dct:subject ?subject . }
    """)


    print(concept.toList)

      // REFACTORIZATION: .map { item => item.getOrElse("subject", "").asInstanceOf[String] }
//      .map { item => item.getOrElse("subject", "").toString() }
//      .map { item =>
//        val uri = item
//        val label = URIHelper.extractLabelFromURI(uri)
//        val lang = ""
//        URIWithLabel(label, uri, lang)
//      }
  }


  def parse_dcat_keywords() = {
    SPARQL(voca.repo).query("""
      PREFIX dcat: <http://www.w3.org/ns/dcat#>
      PREFIX dct: <http://purl.org/dc/terms/>
      PREFIX skos: <http://www.w3.org/2004/02/skos/core#>
      SELECT DISTINCT ?keyword ?lang
      WHERE { ?uri a skos:ConceptScheme . ?uri dcat:keyword ?keyword . BIND(LANG(?keyword) AS ?lang) }
    """)
      .map { item =>
        /*REFACTORIZATION
        val label = item.getOrElse("keyword", "").asInstanceOf[String].trim()
        val lang = item.getOrElse("lang", "").asInstanceOf[String]
        */
        val label = item.getOrElse("keyword", "").toString().trim()
        val lang = item.getOrElse("lang", "").toString().trim()
        val uri = s"keywords://${lang}#${label.replaceAll("\\s", "+")}"
        URIWithLabel(label, uri, lang)
      }
  }


}
