package check

import it.almawave.linkeddata.kb.catalog.ResourcesLoader

object CheckresourceLoader extends App {

  var loader = ResourcesLoader("./conf/catalog.conf")
  //  val _vocabularies = loader.fetchVocabularies(false)
  val _ontologies = loader.fetchOntologies(true)

  _ontologies
    .map { item => item.titles(0) }
    .foreach { item =>
      println(item)
    }

}