package com.z.userservice.transformer

import com.z.userservice.domain.User
import com.z.userservice.dto.UserResponse
import com.z.zcoreblocking.transformer.Transformer
import org.springframework.stereotype.Component

@Component
class UserTransformer: Transformer<User, UserResponse> {

    override fun transform(source: User): UserResponse {
        return UserResponse(
                id = source.id ?: -1L,
                name = source.name,
                state = source.state,
                roles = source.roles,
                createdDate = source.createdDate,
                lastModifiedDate = source.lastModifiedDate,
                createdBy = source.createdBy,
                modifiedBy = source.modifiedBy
        )
    }
}