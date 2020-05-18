package com.z.userservice.service.management

import com.z.userservice.dao.UserDao
import com.z.userservice.dto.DeleteUserRequest
import com.z.userservice.dto.UpdateUserRequest
import com.z.userservice.dto.UserResponse
import com.z.userservice.service.encrypt.EncoderService
import com.z.userservice.transformer.UserDetailsTransformer
import com.z.userservice.transformer.UserTransformer
import com.z.userservice.utils.evaluate
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.validation.Validator

@Service
class UserManagementService(private val userDao: UserDao,
							private val userSearchService: UserSearch,
							private val userDetailsTransformer: UserDetailsTransformer,
							private val userTransformer: UserTransformer,
							private val encoderService: EncoderService,
							private val validator: Validator): UserDetailsService, UserManagement {

    override fun loadUserByUsername(username:String): UserDetails {
		val user = this.userDao.findByName(username)
						       .orElseThrow { UsernameNotFoundException("user $username not found")}
		return this.userDetailsTransformer.transform(user)
	}

	@Transactional
	override fun update(updateUserRequest: UpdateUserRequest): UserResponse {
		validator.evaluate(updateUserRequest)
		val user = userSearchService.findUserById(updateUserRequest.id).apply {
			state = updateUserRequest.state
			if(updateUserRequest.roles.isNotEmpty()){
				roles.apply {//to avoid the delete all query
					removeIf { !updateUserRequest.roles.contains(it) }//remove those the request not contains
					addAll(updateUserRequest.roles.filterNot { roles.contains(it) })//add those the original not contains
				}
			}
			if(updateUserRequest.password != null) {
				this.password = encoderService.encodeIfNotEmpty(updateUserRequest.password!!)
			}
		}
		return userTransformer.transform(userDao.save(user))
	}

	override fun delete(deleteUserRequest: DeleteUserRequest): UserResponse {
		return this.update(UpdateUserRequest(id = deleteUserRequest.id, state = false))
	}
}
