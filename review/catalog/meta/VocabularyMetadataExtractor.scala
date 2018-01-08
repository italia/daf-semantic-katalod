package it.almawave.kb.catalog.meta

import java.net.URL

import org.eclipse.rdf4j.repository.Repository

import it.almawave.kb.catalog.SPARQL
import it.almawave.kb.catalog.file.RDFFileRepository

object VocabularyMetadataExtractor {

  def apply(url: URL) = {

    val repo: Repository = new RDFFileRepository(url)

    repo.initialize()

    val sparql = SPARQL(repo)

    // CHECK: this is an experiment... SEE: RDFFrame class
    val frames = sparql.query("""
      SELECT DISTINCT ?super ?concept ?property 
      WHERE { 
        ?concept a ?klass . ?klass rdfs:subClassOf* ?super . 
        ?concept ?property [] .
      }
      ORDER BY ?super ?concept ?property
    """)
      .map { item => (item.get("concept").get.asInstanceOf[String], item.get("property").get.asInstanceOf[String]) }
      .groupBy { item => item._1 }
      .map { item => (item._1, item._2.toList.map(_._2)) }.toMap

    def parseMeta(): VocabularyMeta = {

      val source: URL = url

      val id: String = url.getPath.replaceAll(".*/(.*)\\.[a-z]+$", "$1").trim()

      val _voc_url = sparql.query("""
        PREFIX skos: <http://www.w3.org/2004/02/skos/core#>
        PREFIX adms: <http://www.w3.org/ns/adms#> 
        SELECT DISTINCT ?uri 
        WHERE { ?uri a adms:SemanticAsset }
      """)

      val voc_url = if (_voc_url.isEmpty) source else new URL(_voc_url(0)("uri").asInstanceOf[String])

      val concepts: Set[String] = sparql.query("""
        PREFIX skos: <http://www.w3.org/2004/02/skos/core#>
        SELECT DISTINCT ?concept 
        WHERE { ?concept a ?klass. ?klass rdfs:subClassOf* skos:Concept . } 
        """)
        .map(_.getOrElse("concept", "owl:Thing").toString()).toSet

      val titles: Seq[(String, String)] = sparql.query("""
        PREFIX skos: <http://www.w3.org/2004/02/skos/core#>
        SELECT DISTINCT * {  ?uri a skos:ConceptScheme . ?uri rdfs:label ?label . BIND(LANG(?label) AS ?lang) }
        """)
        .map { item => (item.get("lang").get.asInstanceOf[String], item.get("label").get.asInstanceOf[String]) }

      // TODO: case class
      val descriptions: Seq[(String, String)] = sparql.query("""
        PREFIX skos: <http://www.w3.org/2004/02/skos/core#>
        SELECT DISTINCT * {  ?uri a skos:ConceptScheme . ?uri rdfs:comment ?comment . BIND(LANG(?comment) AS ?lang) }
        """)
        .map { item => (item.get("lang").get.asInstanceOf[String], item.get("comment").get.asInstanceOf[String]) }

      // TODO: case class
      val version: Seq[(String, String)] = Seq.empty // TODO!

      val creators: Set[String] = Set.empty // TODO!

      VocabularyMeta(
        id,
        voc_url,
        source,
        concepts,
        titles,
        descriptions,
        version,
        creators)

    }

    val meta = parseMeta()

    repo.shutDown()

    meta
  }

}
