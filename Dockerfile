
# JAVA
FROM openjdk:8-jre-alpine

# application
WORKDIR katalod

ADD conf/ conf/
ADD ontologie-vocabolari-controllati/ ontologie-vocabolari-controllati/
ADD src/main/swagger-ui src/main/swagger-ui
ADD target/libs /usr/share/katalod/lib
ADD target/kataLOD-0.0.9.jar /usr/share/katalod/kataLOD-0.0.9.jar

ENTRYPOINT ["/usr/bin/java", "-cp", "/usr/share/katalod/lib/*:/usr/share/katalod/kataLOD-0.0.9.jar", "it.almawave.kb.http.MainHTTP"]

EXPOSE 7777
