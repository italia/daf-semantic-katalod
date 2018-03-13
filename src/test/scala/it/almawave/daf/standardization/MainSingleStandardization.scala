package it.almawave.daf.standardization

import org.slf4j.LoggerFactory
import java.nio.file.Paths
import it.almawave.linkeddata.kb.catalog.CatalogBox
import com.typesafe.config.ConfigFactory
import scala.util.Try
import it.almawave.linkeddata.kb.utils.JSONHelper

object MainSingleStandardization extends App {

  private val logger = LoggerFactory.getLogger(this.getClass)

  val conf = ConfigFactory.parseFile(Paths.get("./conf/catalog.conf").normalize().toFile())

  // REVIEW val conf = ConfigFactory.parseResources("it/almawave/daf/standardization/example_single.conf")

  val catalog = new CatalogBox(conf)
  catalog.start()

  val std = new StandardizationProcess(catalog)

  // DEBUG single
  val vocID = "territorial-classification" // "Licenze" //AccommodationTypology"
  val vbox = std.vocabularyWithDependency(vocID).get

  vbox.start()

  //  val concepts = vbox.concepts
  //  println("\n\n\n\n\nVBOX CONCEPTS?")
  //  println(concepts.map(_._1).mkString(" | "))

  //  val instances = vbox.meta.instances
  //  println("\n\n\n\n\nVBOX INSTANCES?")
  //  println(instances.mkString("\n"))

  val _try = Try {

    logger.info(s"\n\n#### Vocabulary: ${vbox} ####")

    //    val ontos = vbox.infer_ontologies()
    //    println(s"\n\n\nONTOLOGIES FOR <${vocID}>:\n" + ontos.mkString("\n") + "\n\n")
    //    println(".................................................................")

    //    val detected_ontology = std.detect_ontology(vbox)
    //    logger.info(s"\n\n\n\n#### DETECTED ONTOLOGY: ${detected_ontology} ####")

    // DEBUG
    val cells = std.standardizeDataByVocabularyBox(vbox, lang = "it")
    logger.debug(JSONHelper.writeToString(cells.toList))

    val MAX_LEVELS = std.max_levels(vbox).get
    logger.info("MAX_LEVELS: " + MAX_LEVELS)

    logger.info(s"#############################\n\n")
  }

  if (_try.isFailure) {
    val err = _try.failed.get
    System.err.println("ERROR FOR " + vbox)
    System.err.println("ERR: " + err)
    err.printStackTrace(System.err)
  }

  // SEE: Istat-Classificazione-08-Territorio

  vbox.stop()

  catalog.stop()

}