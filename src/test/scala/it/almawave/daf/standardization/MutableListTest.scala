package it.almawave.daf.standardization

import it.almawave.kb.http.models.HierarchyBis

import scala.collection.mutable.ListBuffer

object MutableListTest {

  def main(args: Array[String]): Unit = {

    val ids = List("a5", "b0", "z0", "c9", "d9", "d0", "d5")

    // Use sortBy.
    // ... Create a sort key.
    //     The second char is first.
    //     And the first char second.
    val result = ids.sortBy((x: String) => (x.charAt(1), x.charAt(0)))

    // Print our sorted ids.
//    result.foreach(println(_))


    val _hierarchy1 = HierarchyBis(-1, "2.1", "Licenza Aperta",
      "http://w3id.org/italia/controlled-vocabulary/licences/A_OpenLicense", "", new ListBuffer[HierarchyBis]())

    val _hierarchy2 = HierarchyBis(-1, "2.2", "Pubblico Dominio",
      "http://w3id.org/italia/controlled-vocabulary/licences/A1_PublicDomain", "http://w3id.org/italia/controlled-vocabulary/licences/A_OpenLicense", new ListBuffer[HierarchyBis]())

    val _hierarchy3 = HierarchyBis(-1, "2.8", "Creative Commons CC0 1.0 Universale - Public Domain Dedication (CC0 1.0)",
      "http://w3id.org/italia/controlled-vocabulary/licences/A11_CCO10", "http://w3id.org/italia/controlled-vocabulary/licences/A1_PublicDomain", new ListBuffer[HierarchyBis]())

    val _hierarchy4 = HierarchyBis(-1, "2.10", "Creative Commons CC0 1.0 Universale - Public Domain Dedication (CC0 1.0)",
      "http://w3id.org/italia/controlled-vocabulary/licences/A11_CCO10", "http://w3id.org/italia/controlled-vocabulary/licences/A1_PublicDomain", new ListBuffer[HierarchyBis]())

    val _hierarchy5 = HierarchyBis(-1, "2.3", "Creative Commons CC0 1.0 Universale - Public Domain Dedication (CC0 1.0)",
      "http://w3id.org/italia/controlled-vocabulary/licences/A11_CCO10", "http://w3id.org/italia/controlled-vocabulary/licences/A1_PublicDomain", new ListBuffer[HierarchyBis]())

    val _hierarchy6 = HierarchyBis(-1, "2.4", "Creative Commons CC0 1.0 Universale - Public Domain Dedication (CC0 1.0)",
      "http://w3id.org/italia/controlled-vocabulary/licences/A11_CCO10", "http://w3id.org/italia/controlled-vocabulary/licences/A1_PublicDomain", new ListBuffer[HierarchyBis]())

    val _hierarchy7 = HierarchyBis(-1, "2.11", "Creative Commons CC0 1.0 Universale - Public Domain Dedication (CC0 1.0)",
      "http://w3id.org/italia/controlled-vocabulary/licences/A11_CCO10", "http://w3id.org/italia/controlled-vocabulary/licences/A1_PublicDomain", new ListBuffer[HierarchyBis]())

    val _hierarchy8 = HierarchyBis(-1, "2.9", "Creative Commons CC0 1.0 Universale - Public Domain Dedication (CC0 1.0)",
      "http://w3id.org/italia/controlled-vocabulary/licences/A11_CCO10", "http://w3id.org/italia/controlled-vocabulary/licences/A1_PublicDomain", new ListBuffer[HierarchyBis]())

    val _hierarchy9 = HierarchyBis(-1, "2.12", "Creative Commons CC0 1.0 Universale - Public Domain Dedication (CC0 1.0)",
      "http://w3id.org/italia/controlled-vocabulary/licences/A11_CCO10", "http://w3id.org/italia/controlled-vocabulary/licences/A1_PublicDomain", new ListBuffer[HierarchyBis]())

    val _hierarchy10 = HierarchyBis(-1, "2.13", "Creative Commons CC0 1.0 Universale - Public Domain Dedication (CC0 1.0)",
      "http://w3id.org/italia/controlled-vocabulary/licences/A11_CCO10", "http://w3id.org/italia/controlled-vocabulary/licences/A1_PublicDomain", new ListBuffer[HierarchyBis]())



    val _list1 = new ListBuffer[HierarchyBis]
    val _list2 = new ListBuffer[HierarchyBis]
    val _list3 = new ListBuffer[HierarchyBis]

    _list2 += _hierarchy2
    _list2.copyToBuffer(_hierarchy1.children)
    _list3 += _hierarchy3
    _list3 += _hierarchy4
    _list3 += _hierarchy5
    _list3 += _hierarchy6
    _list3 += _hierarchy7
    _list3 += _hierarchy8
    _list3 += _hierarchy9
    _list3 += _hierarchy10
    _list3.copyToBuffer(_hierarchy2.children)
    _list1 += _hierarchy1

//    println(_list1)
    var _hierarchy_tmp: HierarchyBis = null
    var _list_hierarchy_tmp: ListBuffer[HierarchyBis] = new ListBuffer[HierarchyBis]()
    val _list_hierarchy = _list1.last.children.last.children.toList.sortBy(HierarchyBis => HierarchyBis.codice)
    _list_hierarchy.foreach( item_hierarchy => {

      val _item_hierarchy = item_hierarchy
      println("codice iniziale: " + _item_hierarchy.codice)
      var _app = new StringBuffer("")
//      val codice = _item_hierarchy.codice.replace(".", "&")
//      println("codice iniziale+replace: " + codice)
      _item_hierarchy.codice.toList.foreach{c =>

//        println(c)
        //se numerico
        if(isNumeric(c.toString) || c.toString.equals(".")) {
          _app.append(c.toString)

        }


      }
      println("depurato: " + _app)
      _hierarchy_tmp = HierarchyBis(-1,_app.toString, _item_hierarchy.label, _item_hierarchy.uri, _item_hierarchy.parent_uri, new ListBuffer[HierarchyBis]())
      println("dopo depurato: " + _hierarchy_tmp.codice)
      _list_hierarchy_tmp += _hierarchy_tmp
    })

    println("_list_hierarchy_tmp: " + _list_hierarchy_tmp)

    val sortedDudes = _list_hierarchy_tmp.sortWith(_.codice > _.codice)
    println(sortedDudes)

    sortedDudes.foreach(item =>
      println("list.item.depurato: " + item.codice)
    )


  }

  def isNumeric(input: String): Boolean = input.forall(_.isDigit)
}
