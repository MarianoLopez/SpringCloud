# SpringCloud
This is a SpringCloud example project with some extras like: maven modules, kotlin, jpa 2.0 entity graphs, spring-data, spring-security, spring-integration test, mockito, and so on...
# Artifacts    
* **User-Service**: This is a microservice with a RESTful API that aims to manage (CRUD) the users & login process (generate JWT tokens)
    * Language: Kotlin 1.3.71
    * Framework: Springboot 2.2.6.RELEASE
    * Uses: 
        * Spring-data for the data access layer
            * JPA2.1 Entity-Graphs to solve n+1 sql queries
            * Hibernate as Object Relational Mapper
            * Flyway as database migration library
            * MariaDB as database engine
        * Spring-security for secure endpoints by user roles
        * Spring-mvc for servlet based front controller pattern
            * RestController, HttpMappings, ControllerAdvice (exception handling)
        * Spring-cloud: Eureka client for service registration 
        * Testing
            * Integration tests and JpaTest with Springboot-test module
            * Unit test with Junit5, Mockito as mocking library and Hamcrest as matchers library
        * Internal dependencies: zcore-blocking module
#Libraries
* **JWT**: This is a custom library for use JsonWebTokens among every artifact, knows how to create and decode tokens. Is a spring-boot-configuration-processor which enable the configuration properties for jwt via application.properties file.
    * Language: Kotlin 1.3.61
    * Framework: Springboot 2.2.5.RELEASE

* **Zcore-blocking**: This is a custom bootstrapper library for spring mvc projects (servlet based). Provides auto-configuration for Swagger 3 (API documentation), Secure endpoints (if security module is present) using a JWT filter to catch the authentication tokens and inject them into the SpringSecurityContext.
    * Language: Kotlin 1.3.61
    * Framework: Springboot 2.2.5.RELEASE
    * Internal dependencies: jwt module
     
# Deploy Steps
This project includes a **docker-compose.yml** file to deploy the whole stack (including database), but before that it's necessary generate all the .jar files for each microservice with Maven.
* Build and install locally the jwt-module. In the folder of the jwt-module run: `mvnw clean install`
* Build and install locally the zcore-module. In the folder of the zcore-module run: `mvnw clean install`
* Build, generate .jar and create docker image for user-service module. In the folder of the user-service/scripts run: `buildDockerImage.sh`
* Run the containers. In the folder docker-compose run: `docker-compose up`
