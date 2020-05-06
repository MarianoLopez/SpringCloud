FROM openjdk:14-jdk-slim
ARG JVM_OPTS
ADD target/*.jar eureka-service.jar
ENTRYPOINT java $JVM_OPTS -jar eureka-service.jar
