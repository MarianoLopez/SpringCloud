package com.z.zcoreblocking.utils.swagger

import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiImplicitParams

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.TYPE)
@Retention(AnnotationRetention.RUNTIME)
@ApiImplicitParams(
    ApiImplicitParam(
        name = "Authorization", dataType = "String",
        paramType = "header", defaultValue = "",
        value = "Json Web Token.", required = true,
        allowEmptyValue = false, allowMultiple = false)
)
annotation class AuthorizationHeader