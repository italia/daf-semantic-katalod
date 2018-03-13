package check

import java.io.File
import scala.annotation.tailrec
import it.almawave.linkeddata.kb.catalog.CatalogBox
import java.nio.file.Paths
import com.typesafe.config.ConfigFactory

object MainCatalogList extends App {

  val conf = ConfigFactory.parseFile(new File("./conf/catalog.conf"))
  val catalog = new CatalogBox(conf)
  catalog.start()

//  val git = catalog.GIT
//  git.synchronize()

  val vocabs = catalog.store.vocabularies()
  println("\n\nn° vocabs? " + vocabs.size)
  vocabs.foreach { f =>
    println(f)
  }

  val ontos = catalog.store.ontologies()
  println("\n\nn° ontos? " + ontos.size)
  ontos.foreach { item =>
    println(item)
  }

}