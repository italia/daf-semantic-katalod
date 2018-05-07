package it.almawave.daf.standardization

import it.almawave.linkeddata.kb.catalog.models.Hierarchy
import it.almawave.linkeddata.kb.utils.JSONHelper

import scala.collection.mutable.ListBuffer

object MutableListBufferTest {

  def main(args: Array[String]): Unit = {


    val hierarchy_child = ListBuffer[ListBuffer[scala.collection.mutable.Map[String,Any]]]()
    var tmp_hierarchy_child = ListBuffer[scala.collection.mutable.Map[String,Any]]()

    var states = scala.collection.mutable.Map[String, Any]()
    states += ("IT" -> "Italia")
//    states += ("CO" -> "Colorado", "KY" -> "Kentucky")


    var city = scala.collection.mutable.Map[String, Any]()
    city += ("RM" -> "Roma")
    city += ("CT" -> "Cagliari", "NA" -> "Napoli")


    var fiori = scala.collection.mutable.Map[String, Any]()
    fiori += ("MA" -> "Margherita")
    fiori += ("RO" -> "Rosa", "GI" -> "Girasole")

    var portate = scala.collection.mutable.Map[String, Any]()
    portate += ("CO" -> "Colazione")
    portate += ("PR" -> "Pranzo", "ME" -> "Merenda", "CE" -> "Cena")

    tmp_hierarchy_child += city
    tmp_hierarchy_child += fiori



    var tmp_hierarchy_child2 = new ListBuffer[Hierarchy]()
    //    model_hierarchy = Hierarchy(states, tmp_hierarchy_child)

    var child1_1_1 = Hierarchy(portate.get("CO").get.toString, portate.get("ME").get.toString, portate.get("PR").get.toString, portate.get("CE").get.toString, new ListBuffer[Hierarchy]())
    var child1_1_2 = Hierarchy(portate.get("CE").get.toString, portate.get("PR").get.toString, portate.get("ME").get.toString, portate.get("CO").get.toString, new ListBuffer[Hierarchy]())
    var list_child1_1_1 = new ListBuffer[Hierarchy]()
    list_child1_1_1 += child1_1_1
    list_child1_1_1 += child1_1_2
    var list_child1_1 = new ListBuffer[Hierarchy]()
    var child1_1 = Hierarchy(fiori.get("MA").get.toString, fiori.get("RO").get.toString, fiori.get("GI").get.toString, fiori.get("MA").get.toString, list_child1_1_1)
    list_child1_1 += child1_1
    var child1 = Hierarchy(city.get("RM").get.toString, city.get("CT").get.toString, city.get("NA").get.toString, city.get("RM").get.toString, list_child1_1)

    tmp_hierarchy_child2 += child1


    //    tmp_hierarchy_child2 += Hierarchy(fiori.get("MA").get.toString, fiori.get("RO").get.toString, fiori.get("GI").get.toString, fiori.get("MA").get.toString, List())


    //aggiungo collection a ListBuffer
    //    tmp_hierarchy_child.prepend(city)









//    tmp_hierarchy_child += city

//    println(tmp_hierarchy_child2)

    println(JSONHelper.writeToString(tmp_hierarchy_child2))


  }

}
