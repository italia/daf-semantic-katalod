package it.almawave.daf.standardization

import java.nio.file.Paths
import com.typesafe.config.ConfigFactory
import it.almawave.linkeddata.kb.catalog.VocabularyBox
import java.net.URL
import it.almawave.linkeddata.kb.catalog.OntologyBox

object MainChooseQueryByType extends App {

  //  val source_url = new URL("https://raw.githubusercontent.com/italia/daf-ontologie-vocabolari-controllati/master/VocabolariControllati/Licenze/Licenze.ttl")
  val source_url = new URL("https://raw.githubusercontent.com/italia/daf-ontologie-vocabolari-controllati/master/VocabolariControllati/ClassificazioneTerritorio/Istat-Classificazione-08-Territorio.ttl")
  val onto_src = new URL("https://raw.githubusercontent.com/italia/daf-ontologie-vocabolari-controllati/master/Ontologie/IndirizziLuoghi/latest/CLV-AP_IT.ttl")

  val conf = ConfigFactory.parseFile(Paths.get("./conf/catalog.conf").normalize().toFile()).getConfig("daf.standardization")

  val obox = OntologyBox.parse(onto_src)
  obox.start()

  val vbox = VocabularyBox.parse(source_url).federateWith(Array(obox))
  vbox.start()

  val qstd = new StandardizationQuery(conf)

  val voc_type = vbox.infer_vocabulary_type()
  println(voc_type)

  //  val q_type = vbox.extract_assetType()
  //  println("TYPE? " + q_type)

  //  val hierarchy = qstd.hierarchy(vbox)
  //  println(hierarchy)

  vbox.stop()

}