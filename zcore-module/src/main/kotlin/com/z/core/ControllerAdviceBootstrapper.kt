package com.z.core

import com.z.core.dto.ApiResponse
import com.z.core.utils.groupByFieldMessage
import com.z.core.utils.toApiResponse
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Configuration
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.support.WebExchangeBindException
import org.springframework.web.reactive.config.WebFluxConfigurer
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono

@Configuration
class ControllerAdviceBootstrapper{

	@ConditionalOnClass(WebFluxConfigurer::class)
	@ConditionalOnMissingBean(ControllerAdvice::class)
	@ControllerAdvice
	class DefaultErrorHandler {
		private val logger = LoggerFactory.getLogger(ControllerAdviceBootstrapper::class.java)

		init {
			logger.info("Bootstrapping ControllerAdvice bean")
		}

		@ExceptionHandler(Exception::class)
		fun exceptionHandler(exception: Exception): Mono<ResponseEntity<ApiResponse>> {
			return ResponseEntity.badRequest().body(exception.toApiResponse()).toMono()
		}

		@ExceptionHandler(WebExchangeBindException::class)
		fun validationExceptionHandler(webExchangeBindException: WebExchangeBindException): Mono<ResponseEntity<ApiResponse>> {
			return ResponseEntity.badRequest().body(ApiResponse(
				title = WebExchangeBindException::class.java.simpleName,
				payload = webExchangeBindException.bindingResult.fieldErrors.groupByFieldMessage()
			)).toMono()
		}
	}
}
