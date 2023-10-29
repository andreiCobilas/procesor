FROM eclipse-temurin:17-jdk AS build
ARG projVersion="0.0.1-SNAPSHOT"

WORKDIR /app
COPY . /app

RUN ./gradlew build importAgents -P projVersion=$projVersion

FROM eclipse-temurin:17-jre-alpine
ARG projVersion="0.0.1-SNAPSHOT"

COPY --from=build /app/build/libs/* /
ENV libPath="/procesor-$projVersion.jar"
ENV agentConfig="-Dcom.sun.management.jmxremote.ssl=false -Djava.rmi.server.hostname=localhost -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.port=5556 -Dcom.sun.management.jmxremote.rmi.port=5556 -javaagent:/jmx_prometheus_javaagent-0.17.0.jar=8000:/jmx_prometheus_config.yaml"

CMD ["sh", "-c", "java ${JAVA_OPTS} ${agentConfig} -jar ${libPath}"]