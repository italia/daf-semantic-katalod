# JAVA
FROM openjdk:8-jre-alpine

ENV VERS=0.0.9
RUN apk update && apk add maven git

# maven build
ADD pom.xml mvn_src/pom.xml
ADD src/ mvn_src/src/
ADD lib/ mvn_src/lib/
WORKDIR mvn_src
RUN mvn clean package -Dmaven.test.skip=true

# application
WORKDIR katalod

ADD conf/ conf/
ADD src/main/swagger-ui src/main/swagger-ui
#ADD target/libs /usr/share/katalod/lib
#ADD target/kataLOD-${VERS}.jar /usr/share/katalod/kataLOD-${VERS}.jar

RUN mkdir -p /usr/share/katalod
RUN cp -R /mvn_src/target/libs /usr/share/katalod/lib/
RUN cp /mvn_src/target/kataLOD-${VERS}.jar /usr/share/katalod/kataLOD-${VERS}.jar

# TODO: disable ADD of local files, enable git clone of remote files
ADD ontologie-vocabolari-controllati/ ontologie-vocabolari-controllati/
# RUN git clone https://github.com/italia/daf-ontologie-vocabolari-controllati.git

ENTRYPOINT /usr/bin/java -cp /usr/share/katalod/lib/*:/usr/share/katalod/kataLOD-${VERS}.jar it.almawave.kb.http.MainHTTP

EXPOSE 7777
