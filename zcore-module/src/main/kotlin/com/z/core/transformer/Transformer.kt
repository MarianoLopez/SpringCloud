package com.z.core.transformer

interface Transformer <A, B> {
    fun transform(source: A) : B
}