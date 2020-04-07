! bin/bash
../mvnw clean package -f ../pom.xml
docker build -t gateway-service:snapshot -f ../Dockerfile .