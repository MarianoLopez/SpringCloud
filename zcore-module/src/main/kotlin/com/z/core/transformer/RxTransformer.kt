package com.z.core.transformer

import reactor.core.publisher.Mono

interface RxTransformer <A, B> {
    fun transform(source: A) : Mono<B>
}