package com.z.userservice.utils

import javax.validation.ConstraintViolation
import javax.validation.ConstraintViolationException
import javax.validation.Validator

@Throws(ConstraintViolationException::class)
fun <T> Validator.evaluate(obj: T) {
    val result: Set<ConstraintViolation<T>> = this.validate(obj)
    if (result.isNotEmpty()) {
        throw ConstraintViolationException(result)
    }
}