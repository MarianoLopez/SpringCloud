package com.z.zcoreblocking.transformer

interface Transformer <A, B> {
    fun transform(source: A) : B
}