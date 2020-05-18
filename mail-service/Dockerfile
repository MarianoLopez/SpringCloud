FROM openjdk:14-jdk-slim
ENV JVM_ARGS ''
ENV SPRING_ARGS ''
ADD target/*.jar mail-service.jar
ENTRYPOINT java $JVM_ARGS -jar mail-service.jar $SPRING_ARGS
