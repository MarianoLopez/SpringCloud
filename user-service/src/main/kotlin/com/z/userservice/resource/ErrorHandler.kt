package com.z.userservice.resource


import com.z.zcoreblocking.domain.ApiResponse
import com.z.zcoreblocking.utils.groupByFieldMessage
import com.z.zcoreblocking.utils.toApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.support.WebExchangeBindException

@ControllerAdvice
class ErrorHandler {

	@ExceptionHandler(Exception::class)
	fun exceptionHandler(exception: Exception): ResponseEntity<ApiResponse> {
		return ResponseEntity.badRequest().body(exception.toApiResponse())
	}

	@ExceptionHandler(WebExchangeBindException::class)
	fun validationExceptionHandler(webExchangeBindException: WebExchangeBindException): ResponseEntity<ApiResponse> {
		return ResponseEntity.badRequest().body(ApiResponse(
			title = WebExchangeBindException::class.java.simpleName,
			payload = webExchangeBindException.bindingResult.fieldErrors.groupByFieldMessage()
		))
	}
}