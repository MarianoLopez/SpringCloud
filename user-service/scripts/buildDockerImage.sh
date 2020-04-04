! bin/bash
../mvnw.cmd clean package -f ../pom.xml
docker build -t user-service:snapshot -f ../Dockerfile .