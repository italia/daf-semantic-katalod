
# JAVA
FROM openjdk:8-jre-alpine

# application
WORKDIR katalod

# general configurations
# LABEL it.almawave.daf.katalod.version="0.0.7"
# LABEL vendor="Almawave"
# LABEL it.almawave.daf.katalod.date="2018-05-10"
# ENV kb_version 0.1.2

ADD conf/ conf/
ADD ontologie-vocabolari-controllati/ ontologie-vocabolari-controllati/
ADD src/main/swagger-ui src/main/swagger-ui
ADD target/libs /usr/share/katalod/lib
ADD target/kataLOD-0.0.7.jar /usr/share/katalod/kataLOD-0.0.7.jar

ENTRYPOINT ["/usr/bin/java", "-cp", "/usr/share/katalod/lib/*:/usr/share/katalod/kataLOD-0.0.7.jar", "it.almawave.kb.http.MainHTTP"]

EXPOSE 7777