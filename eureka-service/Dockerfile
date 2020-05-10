FROM openjdk:14-jdk-slim
ENV JVM_ARGS ''
ENV SPRING_ARGS ''
ADD target/*.jar eureka-service.jar
ENTRYPOINT java $JVM_ARGS -jar eureka-service.jar $SPRING_ARGS
