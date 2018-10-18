package it.almawave.kb.http.models

import scala.collection.mutable.ListBuffer

case class HierarchyBis (

  id: Int,
  codice: String,
  label: String,
  uri: String,
  parent_uri: String,
  children: ListBuffer[HierarchyBis]//offspring
)
