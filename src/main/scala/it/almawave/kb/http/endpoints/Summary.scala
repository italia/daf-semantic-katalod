package it.almawave.kb.http.endpoints

import javax.ws.rs.Path
import io.swagger.annotations.Api
import javax.ws.rs.core.Context
import javax.servlet.http.HttpServletRequest
import javax.ws.rs.Produces
import javax.ws.rs.GET
import javax.inject.Inject
import javax.ws.rs.core.MediaType
import io.swagger.annotations.ApiOperation

@Api(tags = Array("catalog"))
@Path("/summary")
class Summary {

  @Inject
  var loader: CatalogService = null

  @GET
  @Produces(Array(MediaType.APPLICATION_JSON))
  @ApiOperation(nickname = "listOntologies", value = "list of all ontologies")
  def all(@Context httpRequest: HttpServletRequest) = {

    val ontologies = loader._ontologies.map(_.id)
    val vocabularies = loader._vocabularies.map(_.id)

    s"""{
      
      "ontologies": {
      
        "size": ${ontologies.size},
        "items": [
          ${ontologies.map(o => s""""${o}"""").mkString(", ")}
        ]
      
      },
      
      "vocabularies": {
      
        "size": ${vocabularies.size},
        "items": [
          ${vocabularies.map(o => s""""${o}"""").mkString(", ")}
        ]
      
      }
      
    }"""

  }

}

/*


object MainCatalogBox extends App {

  val conf = ConfigFactory.parseFile(Paths.get("src/main/resources/conf/catalog.conf").normalize().toFile())

  val catalog = new CatalogBox(conf)
  catalog.start()

  println("\n#### ontologies")
  catalog.ontologies.foreach(println(_))
  println("\n#### vocabularies")
  catalog.vocabularies.foreach(println(_))

  // TODO
  val ontologies_with_deps = catalog.ontologies.map { o => o.withImports() }
  // TEST: ${ontologies_with_deps}

  println()

  catalog.stop()

  def show_concepts() {
    val counted = SPARQL(catalog.repo).query("""
    
    #SELECT (COUNT(?s) AS ?triples) 
    #WHERE { 
    #  ?s ?p ?o .
    #}
    
    SELECT DISTINCT ?graph ?concept 
    WHERE {
    
      { ?s a ?concept }
      UNION 
      { GRAPH ?graph { ?s a ?concept } }
    
    }
    
  """)
      .toStream
      .groupBy { x => x.getOrElse("graph", "") }
      .map { x => (x._1, x._2.toList.map(_.getOrElse("concept", "")).distinct) }
      .map { x => (x._1.toString().replaceAll("^.*[#/](.*?)(\\..*)*$", "$1"), x._2.map(_.toString().replaceAll("^.*[#/](.*)$", "$1"))) }

    println("COUNTED? ")
    counted.foreach { x => println(x) }
  }

}

*/
