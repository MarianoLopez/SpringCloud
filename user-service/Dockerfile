FROM openjdk:11-jdk-slim
ARG JVM_OPTS
ADD target/user-service-0.0.1-SNAPSHOT.jar user-service.jar
ENTRYPOINT java $JVM_OPTS -jar user-service.jar
