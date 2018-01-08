package it.almawave.kb.catalog.meta

import org.eclipse.rdf4j.model.IRI
import org.eclipse.rdf4j.model.Value
import java.net.URL
import org.eclipse.rdf4j.model.Resource

case class RDFData(
  subjects: Set[Resource],
  properties: Set[IRI],
  objects: Set[Value],
  contexts: Set[Resource])

// CHECK: ai fini della navigazione bisogna capire se usare solo titles/descriptions it

case class OntologyMeta(
  id: String,
  source: URL,
  url: URL,
  prefix: String,
  namespace: String,
  concepts: Set[String],
  imports: Set[String],
  titles: Seq[(String, String)],
  descriptions: Seq[(String, String)],
  version: Seq[(String, String)],
  creators: Set[String],
  provenance: Seq[Map[String, Any]])

case class VocabularyMeta(
  id: String,
  url: URL,
  source: URL,
  concepts: Set[String],
  titles: Seq[(String, String)],
  descriptions: Seq[(String, String)],
  version: Seq[(String, String)],
  creators: Set[String])
    

