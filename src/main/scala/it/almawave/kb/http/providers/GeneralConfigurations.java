package it.almawave.kb.http.providers;

import javax.ws.rs.ext.Provider;

import io.swagger.annotations.Contact;
import io.swagger.annotations.Info;
import io.swagger.annotations.License;
import io.swagger.annotations.SwaggerDefinition;

//@formatter:off
@SwaggerDefinition(
	info = @Info(
		description = "katalod API", 
		version = "V0.0.1", 
		title = "katalod API - simple API for RDF ontologies & vocabularies", 
		termsOfService = "TODO: terms of service", 
		contact = @Contact(name = "seralf", email = "al.serafini@almawave.it", url = "http://bitbucket/awave/katalod"), 
		license = @License(name = "Apache 2.0", url = "http://bitbucket/awave/katalod")
	),
	host = "localhost:7777",
	basePath = "/kb/api/v1"
)
@Provider
public interface GeneralConfigurations {}
//@formatter:on