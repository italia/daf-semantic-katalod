package it.almawave.daf.standardization

import java.nio.file.Paths

object TestURI {


  def main(args: Array[String]): Unit = {


//    val fileRdf = Paths.get(vboxid.toURI).toString

    val fileRdf = Paths.get("C:/Users/a.mauro/IdeaProjects/katalod/ontologie-vocabolari-controllati/VocabolariControllati/licences/licences.rdf").toString
    println(fileRdf)
//    val fileQuerySparql = Paths.get("C:/Users/a.mauro/IdeaProjects/katalod/conf/query/skos/hierarchyOneLevel.sparql").toFile
//    val skos_url = new URL(s"file:///$fileRdf")
//    val lines: List[String] = Source.fromFile(fileQuerySparql).getLines.toList

//    val voca = VocabularyBox.parse(skos_url)
//    voca.start()
//
//    val concepts = SPARQL(voca.repo).query( lines.toStream.mkString )


    /*concepts.toList.foreach { concept =>
      println(concept)
    }*/
  }

}
