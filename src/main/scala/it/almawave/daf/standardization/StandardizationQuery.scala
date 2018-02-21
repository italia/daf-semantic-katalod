package it.almawave.daf.standardization

import com.typesafe.config.Config
import java.nio.file.Paths
import java.nio.file.Files
import it.almawave.linkeddata.kb.catalog.VocabularyBox

/*
 * this is an helper class for handling the query pairs needed for the standardization datasets
 */
class StandardizationQuery(conf: Config) {

  val config_dir = Paths.get(conf.root().origin().filename()).toFile().getParent

  val default_query_hierarchy: String = resolve_query(conf.getString("query.hierarchy"))
  val default_query_details: String = resolve_query(conf.getString("query.details"))

  def resolve_query(q_conf: String): String = {
    val q_hierarchy_path = Paths.get(config_dir, q_conf).normalize().toAbsolutePath()
    new String(Files.readAllBytes(q_hierarchy_path))
  }

  /*
   * CHECK: for each vocabulary if custom queries exist:
   * 	-	query.hierarchy
   * 	-	query.details
   * When the pair exists, it should overrides the default one.
   */

  /**
   * We can extract the path (a local hierarchy)
   * to each instance (leaf) of a target concept in the vocabulary.
   * The idea is to construct a list of paths composed of URIs,
   * which will be expanded with their details in a second phase.
   */
  def hierarchy(vocbox: VocabularyBox) = {

    val voc_type: String = vocbox.extract_assetType()._1

    // TODO: if (voc_type.equals("http://purl.org/adms/representationtechnique/SKOS"))

    default_query_hierarchy

    // TODO: review the usage of rank
  }

  /**
   * This method internally uses a query to expand details about an individual.
   * We need to use some convention, in order to handle the metadata for internal usage:
   *  + _type_{field}
   *  	is used for describing the datatype for each cell/column, inferred from SPARQL/RDF.
   *  	TODO: conversion of the RDF4J datatypes to a selection of meaningful Java corrispondences
   * 	+ _meta1_{field}
   * 		is used for the metadata level 1, eg: `SKOS.Concept.notation`
   * 	+ _meta2 is actually _meta2_{field}
   * 		is used for the metadata level 2, eg: `Licenze.level1`
   */
  def details(vocBox: VocabularyBox, level: Int, uri: String, lang: String) = {

    val vocabularyID: String = vocBox.id

    // TODO: if (voc_type.equals("http://purl.org/adms/representationtechnique/SKOS"))

    // TODO: generalize injection of parameters (typically using maps)
    default_query_details
      .replace("${vocabularyID}", vocabularyID)
      .replace("${level}", level.toString())
      .replace("${uri}", uri)
      .replace("${lang}", lang)

  }

}