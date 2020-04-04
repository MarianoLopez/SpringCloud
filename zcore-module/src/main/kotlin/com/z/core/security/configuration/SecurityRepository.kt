package com.z.core.security.configuration

import com.z.jwt.service.JwtSpringService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono

class SecurityRepository(private val jwtService: JwtSpringService) : ServerSecurityContextRepository {
	private val logger = LoggerFactory.getLogger(SecurityRepository::class.java)

	override fun save(p0: ServerWebExchange?, p1: SecurityContext?): Mono<Void> {
		throw UnsupportedOperationException("Not supported yet.")
	}

	override fun load(swe: ServerWebExchange): Mono<SecurityContext> {
		logger.debug("${swe.request.path}")//TODO move to interceptor
		swe.request.headers.getFirst(HttpHeaders.AUTHORIZATION)?.let {
			if(it.startsWith(jwtService.prefix)){
				return jwtService.getAuthentication(it).toMono().apply {
					this.subscribe {context-> logger.debug("${context.authentication}") }
				}
			}
		}
		return Mono.empty()
	}
}