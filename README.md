# SpringCloud
This is a SpringCloud example project with some extras like: maven modules, kotlin, jpa 2.0 entity graphs, spring-data, spring-security, spring-integration test, mockito, and so on...
# Requirements
* Java jdk: 14
* Docker: 19.03.1 
* Docker compose: 1.24.1   
# Artifacts    
* **User-Service**: This is a microservice with a RESTful API that aims to manage (CRUD) the users & login process (generate JWT tokens)
    * Language: Kotlin 1.3.71
    * JDK: 8
    * Framework: Springboot 2.2.6.RELEASE
    * Uses: 
        * Spring-data for the data access layer
            * JPA2.1 Entity-Graphs to solve n+1 sql queries
            * Hibernate as Object Relational Mapper
            * Flyway as database migration library
                * Contains sql scripts to bootstrap the USERS database
            * MariaDB as database engine
        * Spring-security for secure endpoints by user roles
        * Spring-mvc for servlet based front controller pattern
            * RestController, HttpMappings, ControllerAdvice (exception handling)
        * Spring-cloud: Eureka client for service registration 
        * Testing
            * Integration tests and JpaTest with Springboot-test module
            * Unit test with Junit5, Mockito as mocking library and Hamcrest as matchers library
        * Internal dependencies: zcore-blocking module
* **Eureka-Service**: Eureka is a REST based service that is primarily for locating services for
 the purpose of load balancing and failover of middle-tier servers.
    * Language: Java 14
    * Framework: Springboot 2.2.6.RELEASE
    * Uses:
        * Spring-cloud: Eureka server
* **Gateway-Service**: This is is a gateway or edge service that provides capabilities
 for dynamic routing, monitoring, resiliency, security, and more.
    * Language: Java 14
    * Framework: Springboot 2.2.6.RELEASE
    * Uses:
        * Spring-cloud: Eureka client           
#Libraries
* **JWT**: This is a custom library for use JsonWebTokens among every artifact, knows how to create and decode tokens. Is a spring-boot-configuration-processor which enable the configuration properties for jwt via application.properties file.
    * Language: Kotlin 1.3.61
    * JDK: 8
    * Framework: Springboot 2.2.5.RELEASE

* **Zcore-blocking**: This is a custom bootstrapper library for spring mvc projects (servlet based). Provides auto-configuration for Swagger 3 (API documentation), Secure endpoints (if security module is present) using a JWT filter to catch the authentication tokens and inject them into the SpringSecurityContext.
    * Language: Kotlin 1.3.61
    * JDK: 8
    * Framework: Springboot 2.2.5.RELEASE
    * Internal dependencies: jwt module
     
# Deploy Steps
This project includes a `docker-compose/docker-compose.yml` file to deploy the whole stack (including database),
but before that it's necessary generate all the .jar files for each microservice with Maven.
* Set the environment variables in `docker-compose/.env` you can use `docker-compose/example.env` as example.
* In the root project folder run: `./buildServices.sh`, this script will compile every microservice
and generate the docker images.
* Run the containers. In the folder docker-compose (`cd docker-compose/`) run: `docker-compose up`
