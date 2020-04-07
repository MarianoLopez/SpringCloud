FROM openjdk:14-jdk-slim
ARG JVM_OPTS
ADD target/gateway-service-0.0.1-SNAPSHOT.jar gateway-service.jar
ENTRYPOINT java $JVM_OPTS -jar gateway-service.jar
