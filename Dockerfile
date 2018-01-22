FROM java

ADD target/libs /usr/share/katalod/lib
ADD target/kataLOD-0.0.1.jar /usr/share/katalod/kataLOD-0.0.1.jar

ENV CLASSPATH=/usr/share/katalod/lib/*:/usr/share/katalod/kataLOD-0.0.1.jar

ENTRYPOINT ["java", "-cp", "/usr/share/katalod/lib/*:/usr/share/katalod/kataLOD-0.0.1.jar", "it.almawave.kb.http.MainHTTP"]

# EX: java -cp "target/kataLOD-0.0.1.jar;target/libs/*" it.almawave.kb.http.MainHTTP

EXPOSE 7777

CMD []