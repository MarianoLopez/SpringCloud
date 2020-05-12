FROM openjdk:14-jdk-slim
ENV JVM_ARGS ''
ENV SPRING_ARGS ''
ADD target/*.jar gateway-service.jar
ENTRYPOINT java $JVM_ARGS -jar gateway-service.jar $JVM_ARGS
