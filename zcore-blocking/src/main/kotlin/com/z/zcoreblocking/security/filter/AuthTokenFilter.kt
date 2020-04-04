package com.z.zcoreblocking.security.filter

import com.fasterxml.jackson.databind.ObjectMapper
import com.z.jwt.service.JwtSpringService
import com.z.zcoreblocking.domain.ApiResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthTokenFilter(private val jwtService: JwtSpringService,
                      private val objectMapper: ObjectMapper): OncePerRequestFilter() {
    private val _logger = LoggerFactory.getLogger(AuthTokenFilter::class.java)

    override fun doFilterInternal(p0: HttpServletRequest, p1: HttpServletResponse, p2: FilterChain) {
        getAuthentication(p0, p1, p2)
    }

    private fun getAuthentication(req: HttpServletRequest, res: HttpServletResponse, filterChain: FilterChain){
        val token = req.getHeader(HttpHeaders.AUTHORIZATION)
        if (!token.isNullOrEmpty()) {
            try {
                val context = jwtService.getAuthentication(token)
                _logger.debug("${context.authentication}")
                SecurityContextHolder.getContext().authentication = context.authentication
                filterChain.doFilter(req, res)
            } catch(exception: RuntimeException ){
                _logger.debug("auth: ${exception.message?:exception.toString()}")
                res.status = HttpServletResponse.SC_BAD_REQUEST
                res.addHeader("Content-Type", "application/json")
                res.writer.write(objectMapper.writeValueAsString(
                    ApiResponse(title = "auth error", payload = exception.message
                        ?: exception.toString()))
                )
            }
        } else {
            filterChain.doFilter(req, res)
        }
    }
}