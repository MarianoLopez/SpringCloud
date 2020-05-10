FROM openjdk:14-jdk-slim
ENV JVM_OPTS ''
ENV SPRING_OPTS ''
ADD target/*.jar user-service.jar
ENTRYPOINT java $JVM_OPTS -jar user-service.jar $SPRING_OPTS
