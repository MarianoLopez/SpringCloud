package com.z.core

import com.z.core.swagger.SwaggerProperties
import com.z.core.swagger.createDocket
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux
import springfox.documentation.swagger2.web.Swagger2ControllerWebFlux
import springfox.documentation.swagger2.web.Swagger2ControllerWebMvc

@Configuration
@EnableConfigurationProperties(SwaggerProperties::class)
class SwaggerBootstrapper {

    @ConditionalOnClass(Swagger2ControllerWebFlux::class)
    @ConditionalOnMissingBean(Docket::class)
    @EnableSwagger2WebFlux
    class SwaggerFluxBoot{
        private val logger: Logger = LoggerFactory.getLogger(SwaggerBootstrapper::class.java)

        @Bean
        fun swagger(swaggerProperties: SwaggerProperties): Docket {
            logger.debug("Bootstrapping Swagger2WebFlux bean with $swaggerProperties")
            return createDocket(swaggerProperties)
        }
    }

    @ConditionalOnClass(Swagger2ControllerWebMvc::class)
    @ConditionalOnMissingBean(Docket::class)
    @EnableSwagger2WebFlux
    class SwaggerBoot{
        private val logger: Logger = LoggerFactory.getLogger(SwaggerBootstrapper::class.java)

        @Bean
        fun swagger(swaggerProperties: SwaggerProperties): Docket {
            logger.debug("Bootstrapping Swagger2WebMvc bean with $swaggerProperties")
            return createDocket(swaggerProperties)
        }
    }
}