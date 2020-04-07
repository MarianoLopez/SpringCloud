! bin/bash
../mvnw clean package -f ../pom.xml
docker build -t eureka-service:snapshot -f ../Dockerfile .