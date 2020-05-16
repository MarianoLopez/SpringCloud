package com.z.jwt.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTCreationException
import com.auth0.jwt.exceptions.JWTVerificationException
import com.z.jwt.dto.TokenRequest
import com.z.jwt.dto.TokenResponse
import com.z.jwt.property.JwtProperties
import com.z.jwt.utils.asDate
import com.z.jwt.utils.asLocalDateTime


open class JwtService(val jwtProperties: JwtProperties = JwtProperties()){
	private val algorithm = Algorithm.HMAC512(jwtProperties.secretKey)

	@Throws(JWTCreationException::class)
	open fun createToken(tokenRequest: TokenRequest): TokenResponse {
		val token = JWT.create().apply {
			withSubject(tokenRequest.subject)
			withIssuer(jwtProperties.issuer)
			withExpiresAt(tokenRequest.expiration.asDate())
			tokenRequest.claims.entries.forEach { claim->  withArrayClaim(claim.key, claim.value)}
		}.sign(algorithm)
		return TokenResponse(
			token = jwtProperties.prefix + token, expires = tokenRequest.expiration,
			subject = tokenRequest.subject, claims = tokenRequest.claims
		)
	}

	@Throws(JWTVerificationException::class)
	open fun decode(token:String): TokenResponse {
		val verifier = JWT.require(algorithm).withIssuer(jwtProperties.issuer).build()
		val jwt = verifier.verify(token.replace(jwtProperties.prefix, "").trim { it <= ' ' })
		return TokenResponse(
				token = jwt.token, expires = jwt.expiresAt.asLocalDateTime(),
				subject = jwt.subject, claims = jwt.claims.mapValues { it.value.asArray(String::class.java) }
		)
	}
}