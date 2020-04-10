package com.z.userservice.resource

import org.junit.jupiter.api.MethodDescriptor
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.MethodOrdererContext
import org.springframework.http.HttpMethod

class HttpTestCustomOrder: MethodOrderer {
    override fun orderMethods(context: MethodOrdererContext) {
        context.methodDescriptors.sortWith(Comparator<MethodDescriptor> { o1, o2 ->
            getOrder(o1.method.name.toUpperCase()) - getOrder(o2.method.name.toUpperCase())
        })
    }

    private fun getOrder(name: String): Int {
       return when {
            name.contains(HttpMethod.GET.name) -> 0
            name.contains(HttpMethod.POST.name) -> 1
            name.contains(HttpMethod.PUT.name) -> 2
            name.contains(HttpMethod.DELETE.name) -> 3
            else -> 4
       }
    }
}