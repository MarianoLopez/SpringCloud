package com.z.zcoreblocking.swagger

import springfox.documentation.builders.ParameterBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.schema.ModelRef
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket

fun createDocket(prop: SwaggerProperties): Docket {
    return Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(prop.basePath?.let { RequestHandlerSelectors.basePackage(it) } ?: RequestHandlerSelectors.any())
        .paths(PathSelectors.any())
        .build()
        .globalOperationParameters(listOf(
            ParameterBuilder()
                .name("Authorization")
                .description("Authorization token")
                .modelRef(ModelRef("string"))
                .parameterType("header")
                .required(false)
                .build()
        ))
}