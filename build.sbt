//import CommonBuild._
import com.typesafe.sbt.packager.docker.{Cmd, ExecCmd}
import de.heikoseeberger.sbtheader.license.Apache2_0
import sbt.Keys.resolvers

organization := "it.almawave"

name := "katalod"

version in ThisBuild := "0.0.2"

//val playVersion = "2.5.14"

val port = 7777

// default port
//PlayKeys.playDefaultPort := port


lazy val root = (project in file(".")).enablePlugins(DockerPlugin)



scalaVersion := "2.11.8"

crossPaths := false

libraryDependencies ++= Seq(

	"org.scalacheck" %% "scalacheck" % "1.12.4" % Test,
	"org.scalatest" %% "scalatest" % "2.2.2" % Test,
	"com.novocode" % "junit-interface" % "0.11" % Test,
	"junit" % "junit" % "4.11" % Test,
	
	"ch.qos.logback" % "logback-classic" % "1.2.3" % "test",
	"org.slf4j"% "slf4j-log4j12" % "1.7.10",
	"org.apache.logging.log4j" % "log4j-core" % "2.8.2",
	"commons-logging" % "commons-logging" % "1.2",

	"com.fasterxml.jackson.core" % "jackson-core" % "2.8.9",
	"com.fasterxml.jackson.core" % "jackson-databind" % "2.8.9",
	"com.fasterxml.jackson.core" % "jackson-annotations" % "2.8.9",
	"com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.8.9",

	"org.eclipse.rdf4j" % "rdf4j-runtime" % "2.2.2",
	"org.eclipse.rdf4j" %  "rdf4j-repository-sail" % "2.2.2",
	"org.eclipse.rdf4j" % "rdf4j-repository-api" % "2.2.2",
	"org.eclipse.rdf4j" % "rdf4j-sail-memory" % "2.2.2",
	"org.eclipse.rdf4j" % "rdf4j-sail-nativerdf" % "2.2.2",
	"com.github.jsonld-java" % "jsonld-java" % "0.9.0",
	
	"org.apache.solr" % "solr-core" % "5.1.0",
	"org.apache.solr" % "solr-solrj" % "5.1.0",
	
	"com.typesafe" % "config" % "1.2.1",
	"org.glassfish.jersey.containers" % "jersey-container-servlet" % "2.24",
	"org.eclipse.jetty" % "jetty-server" % "9.4.0.M1",
	"org.eclipse.jetty" % "jetty-servlet" % "9.4.0.M1",
	"org.eclipse.jetty" % "jetty-util" % "9.4.0.M1",
	"org.eclipse.jetty" % "jetty-http" % "9.4.0.M1",
	"org.eclipse.jetty" % "jetty-io" % "9.4.0.M1",
	// "org.eclipse.jetty" % "jetty-webapp" % "8.1.7.v20120910" % "container,compile",
	// "org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016" % "container,compile" artifacts Artifact("javax.servlet", "jar", "jar"))
	"org.glassfish.jersey.media" % "jersey-media-moxy" % "2.24",
	"org.glassfish.jersey.media" % "jersey-media-json-jackson" % "2.24",
	"com.google.guava" % "guava" % "20.0",
	
	"io.swagger" % "swagger-jersey2-jaxrs" % "1.5.17",
	"io.swagger" %% "swagger-scala-module" % "1.0.4",
	"org.webjars" % "swagger-ui" % "3.0.7",
	
	"it.almawave.linkeddata.kb" % "kbaselib" % "0.0.2" changing()
)


resolvers ++= Seq(
	Resolver.mavenLocal,
	"Maven2 Local" at Path.userHome.asFile.toURI.toURL + ".m2/repository/",
//	"scalaz-bintray" at "http://dl.bintray.com/scalaz/releases",
	"jeffmay" at "https://dl.bintray.com/jeffmay/maven"
)

headers := Map(
  "sbt" -> Apache2_0("2017", "TEAM PER LA TRASFORMAZIONE DIGITALE"),
  "scala" -> Apache2_0("2017", "TEAM PER LA TRASFORMAZIONE DIGITALE"),
  "conf" -> Apache2_0("2017", "TEAM PER LA TRASFORMAZIONE DIGITALE", "#"),
  "properties" -> Apache2_0("2017", "TEAM PER LA TRASFORMAZIONE DIGITALE", "#"),
  "yaml" -> Apache2_0("2017", "TEAM PER LA TRASFORMAZIONE DIGITALE", "#")
)

dockerBaseImage := "anapsix/alpine-java:8_jdk_unlimited"
dockerCommands := dockerCommands.value.flatMap {
  case cmd@Cmd("FROM", _) => List(cmd,
    Cmd("RUN", "apk update && apk add bash krb5-libs krb5"),
    Cmd("RUN", "ln -sf /etc/krb5.conf /opt/jdk/jre/lib/security/krb5.conf")
  )
  case other => List(other)
}

dockerCommands += ExecCmd("ENTRYPOINT", "java", "it.almawave.kb.http.MainHTTP", "-Dconfig.file=conf/production.conf")
//ENTRYPOINT ["java", "-cp", "/usr/share/katalod/lib/*:/usr/share/katalod/kataLOD-0.0.1.jar", "it.almawave.kb.http.MainHTTP"]
dockerExposedPorts := Seq(port)
dockerRepository := Option("10.98.74.120:5000")


// WART
// wartremoverErrors ++= Warts.unsafe
// Wart Remover Plugin Configuration
// wartremoverErrors ++= Warts.allBut(Wart.Nothing, Wart.PublicInference, Wart.Any, Wart.Equals)
// wartremoverExcluded ++= getRecursiveListOfFiles(baseDirectory.value / "target" / "scala-2.11" / "routes").toSeq

