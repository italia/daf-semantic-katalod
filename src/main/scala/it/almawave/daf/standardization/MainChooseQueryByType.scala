package it.almawave.daf.standardization

import java.nio.file.Paths
import com.typesafe.config.ConfigFactory
import it.almawave.linkeddata.kb.catalog.VocabularyBox
import java.net.URL
import it.almawave.linkeddata.kb.catalog.OntologyBox
import java.io.File

object MainChooseQueryByType extends App {

  val voc_src = Paths.get("./src/test/resources/data/CLV-AP_IT.ttl").normalize().toUri().toURL()

  val onto_src = Paths.get("./src/test/resources/data/Istat-Classificazione-08-Territorio.ttl").normalize().toUri().toURL()

  val conf = ConfigFactory.parseFile(Paths.get("./conf/catalog.conf").normalize().toFile()).getConfig("daf.standardization")

  val obox = OntologyBox.parse(onto_src)
  obox.start()

  val vbox = VocabularyBox.parse(voc_src).federateWith(Array(obox))
  vbox.start()

  val qstd = new StandardizationQuery(conf)

  val onto_id = vbox.infer_ontologies()(0)
  println(onto_id)

  val hierarchy = qstd.hierarchy(vbox)
  println(hierarchy)

  vbox.stop()

}