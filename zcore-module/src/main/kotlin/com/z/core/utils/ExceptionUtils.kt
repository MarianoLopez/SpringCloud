package com.z.core.utils

import com.z.core.dto.ApiResponse
import org.springframework.validation.FieldError

fun Exception.toApiResponse(): ApiResponse {
    return ApiResponse(title = this::class.simpleName ?: "exception", payload = this.localizedMessage)
}

fun List<FieldError>.groupByFieldMessage() = this.groupBy({it.field},{it.defaultMessage})