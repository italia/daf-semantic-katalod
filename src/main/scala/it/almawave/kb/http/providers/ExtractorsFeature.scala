package it.almawave.kb.http.providers

import javax.ws.rs.core.Feature
import javax.ws.rs.core.FeatureContext
import javax.ws.rs.ext.Provider
import javax.inject.Singleton
import it.almawave.kb.catalog.ResourcesLoader
import javax.ws.rs.core.Context
import javax.ws.rs.container.ContainerRequestFilter
import javax.ws.rs.container.ContainerRequestContext

//class ExtractorsFeature extends Feature {
//
//  override def configure(context: FeatureContext): Boolean = {
//    context.register(null)
//    true
//  }
//
//}

//@Singleton
//@Provider
//class Extractors extends ContainerRequestFilter {
//
//  override def filter(context: ContainerRequestContext) {
//    println("TODO: filter...." + context)
//  }
//
//  @Context
//  val resourcesLoader = ResourcesLoader("it/almawave/kb/catalog/catalog.conf")
//
//  println("number of ontologies: " + resourcesLoader.fetchOntologies(false).size)
//
//}
