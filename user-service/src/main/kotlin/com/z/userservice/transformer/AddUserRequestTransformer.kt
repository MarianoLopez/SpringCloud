package com.z.userservice.transformer

import com.z.userservice.domain.User
import com.z.userservice.dto.AddUserRequest
import com.z.zcoreblocking.transformer.Transformer
import org.springframework.stereotype.Component

@Component
class AddUserRequestTransformer: Transformer<AddUserRequest, User> {

    override fun transform(source: AddUserRequest): User {
        return User(password = source.password, name = source.name)
    }

}