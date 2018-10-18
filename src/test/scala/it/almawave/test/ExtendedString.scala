package it.almawave.test

class ExtendedString(s:String) {
  def isNumber: Boolean = s.matches("[+-]?\\d+.?\\d+")
}

// and this is the companion object that provides the implicit conversion
object ExtendedString {
  implicit def String2ExtendedString(s:String) = new ExtendedString(s)
}

import ExtendedString._
object IsNumber extends App {
  println("Implicit 1: " + "123".isNumber)
  println("Implicit 2:" + "123a".isNumber)
  println("Implicit 3:" + "123.55".isNumber)
  println("Implicit 4:" + "9".isNumber)

  val l = List("1", "3", "awdf", "123")
  println("Elements in list: "  + l.forall(_.isNumber))
}
