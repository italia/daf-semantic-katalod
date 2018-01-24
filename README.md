katalod
====================

This module is designed to offer support for parsing ontologies and vocabularies, eventually exposing the minimum set of metadata to the other components.

For simplicity it is designed re-using jersey and swagger with jetty, using RDF4J as the main interface to RDF.


## maven build / install

```bash
mvn clean install
```

**CHECK**: in order to use the library as an sbt dependency, we should create a proper naming convention for the artifact, such as: `{artifact}_{scalaVersion}.jar`

## maven test

in order to run the application locally after a mvan build, for easy testing, we can:

```bash
mvn clean package

java -cp "target/kataLOD-0.0.1.jar;target/libs/*" it.almawave.kb.http.MainHTTP
```

----

### dockerization

It's possible to create a docker image directly using the drafted `Dockefile`.

```bash
mvn clean package
sudo docker build . -t katalod:0.0.1
```

In order to run a new container from the generated build, we can use the following command:
```bash
sudo docker run -p 7777:7777 katalod:0.0.1
```

**NOTE**: currently the port 7777 is used as the default port


* * *

## TODO
- [ ] improve of configuration handling, using the external file
- [ ] check: creating Dockerfile / docker image directly from maven build
- [ ] - [ ] enable the generation of resources list (ontologies / vocabularies) directly from github (see tests)
- [ ] add detail API for vocabularies (with DCAT, see tests)
- [ ] add detail API for ontologies (with VOWL, see tests)
- [ ] enable solr for improve API performance, filtering, navigations (see tests)
- [ ] check: simple HTML pages for testing?
- [ ] check: netty instead of jetty?
- [ ] check: replace maven with sbt?
- [ ] check: rewrite with play?
- [ ] check: better handling / mapping of swagger port when using docker

**NOTE**: we could think about changing jetty with play! for the endpoints
