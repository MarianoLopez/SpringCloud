FROM openjdk:14-jdk-slim
ARG JVM_OPTS
ADD target/*.jar user-service.jar
ENTRYPOINT java $JVM_OPTS -jar user-service.jar
