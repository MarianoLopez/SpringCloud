package com.z.zcoreblocking.dto.property

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "request.auth")
data class RequestAuthProperties(var doNotEval:Array<String> = arrayOf("/swagger-ui.html","/v2/api-docs","/favicon.ico")) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RequestAuthProperties

        if (!doNotEval.contentEquals(other.doNotEval)) return false

        return true
    }

    override fun hashCode(): Int {
        return doNotEval.contentHashCode()
    }
}