katalod
====================

This module is designed to offer support for parsing ontologies and vocabularies, eventually exposing the minimum set of metadata to the other components.

For simplicity it is designed re-using jersey and swagger with jetty, using RDF4J as the main interface to RDF.


## maven build / install

```
mvn clean install
```

**CHECK**: in order to use the library as an sbt dependency, we should create a proper naming convention for the artifact, such as: `{artifact}_{scalaVersion}.jar`

## maven test

in order to run the application locally after a mvan build, for easy testing, we can:

```bash
mvn clean package

java -cp "target/kataLOD-0.0.1.jar;target/libs/*" it.almawave.kb.http.MainHTTP
```

TODO: add dockerization of this process
TODO: expose configuration (including port)


## TODO 
+ replace maven with sbt
+ check how to pass port to jetty externally

**NOTE**: we could think about changing jetty with play! for the endpoints

### dockerization
CHECK
https://runnable.com/docker/java/dockerize-your-java-application



