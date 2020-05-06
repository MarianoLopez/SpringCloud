FROM openjdk:14-jdk-slim
ARG JVM_OPTS
ADD target/*.jar gateway-service.jar
ENTRYPOINT java $JVM_OPTS -jar gateway-service.jar
