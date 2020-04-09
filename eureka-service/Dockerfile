FROM openjdk:14-jdk-slim
ARG JVM_OPTS
ADD target/eureka-service-0.0.1-SNAPSHOT.jar eureka-service.jar
ENTRYPOINT java $JVM_OPTS -jar eureka-service.jar
