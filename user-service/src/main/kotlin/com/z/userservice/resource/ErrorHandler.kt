package com.z.userservice.resource


import com.z.zcoreblocking.dto.ApiResponse
import com.z.zcoreblocking.utils.exception.groupByFieldMessage
import com.z.zcoreblocking.utils.exception.toApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import javax.validation.ConstraintViolationException


@ControllerAdvice
class ErrorHandler {

	@ExceptionHandler(Exception::class)
	fun exceptionHandler(exception: Exception): ResponseEntity<ApiResponse> {
		return ResponseEntity.badRequest().body(exception.toApiResponse())
	}

	@ExceptionHandler(ConstraintViolationException::class)
	fun handleConstraintViolation(e: ConstraintViolationException): ResponseEntity<ApiResponse> {
		val fieldErrors = e.constraintViolations
				.map { FieldError(it.rootBeanClass.simpleName, it.propertyPath.toString(), it.message) }
		return ResponseEntity.badRequest().body(ApiResponse(
				title = ConstraintViolationException::class.java.simpleName,
				payload = fieldErrors.groupByFieldMessage()
		))
	}
}