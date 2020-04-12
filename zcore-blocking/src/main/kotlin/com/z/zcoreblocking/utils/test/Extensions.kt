package com.z.zcoreblocking.utils.test

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.util.CollectionUtils.toMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import java.nio.charset.StandardCharsets


fun MockMvc.get(url:String, contentType: MediaType = MediaType.APPLICATION_JSON): ResultActions {
    return this.perform(MockMvcRequestBuilders.get(url).contentType(contentType))
}

inline fun <reified T:Any> ResultActions.get(objectMapper: ObjectMapper = jacksonObjectMapper()): T {
    return objectMapper.readValue(this.andReturn().response.contentAsString)
}

inline fun <reified T:Any> RestTemplate.get(uri:String):ResponseEntity<T> {
    return this.exchange(
            uri,
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<T>() {})
}

inline fun <reified T:Any> RestTemplate.post(
                                    uri:String,
                                    body: Any = Any(),
                                    headers:MultiValueMap<String,String> = toMultiValueMap(emptyMap())):ResponseEntity<T> {
    return this.exchange(
            uri,
            HttpMethod.POST,
            org.springframework.http.HttpEntity(
                body,
                HttpHeaders(headers.apply { addIfAbsent("Content-Type", MediaType.APPLICATION_JSON_VALUE ) })),
            object : ParameterizedTypeReference<T>() {})
}

fun MockHttpServletRequestBuilder.body(
                                    body:Any,
                                    objectMapper: ObjectMapper = jacksonObjectMapper(),
                                    mediaType:MediaType = MediaType.APPLICATION_JSON): MockHttpServletRequestBuilder {
    return this.apply {
        content(objectMapper.writeValueAsString(body))
        contentType(mediaType)
        accept(mediaType)
        characterEncoding(StandardCharsets.UTF_8.name())
    }
}