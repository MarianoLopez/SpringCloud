FROM openjdk:14-jdk-slim
ENV JVM_ARGS ''
ENV SPRING_ARGS ''
ADD target/*.jar user-service.jar
ENTRYPOINT java $JVM_ARGS -jar user-service.jar $SPRING_ARGS
