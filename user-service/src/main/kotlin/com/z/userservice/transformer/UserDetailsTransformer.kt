package com.z.userservice.transformer

import com.z.userservice.domain.User
import com.z.zcoreblocking.transformer.Transformer
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component

@Component
class UserDetailsTransformer: Transformer<User, UserDetails> {

    override fun transform(source: User): UserDetails {
        return org.springframework.security.core.userdetails.User.builder()
            .username(source.id.toString())
            .password(source.password)
            .disabled(!source.state)
            .authorities(source.roles.map { SimpleGrantedAuthority(it.role) })
            .build()
    }

}