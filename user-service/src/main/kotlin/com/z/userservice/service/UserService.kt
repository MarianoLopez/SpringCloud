package com.z.userservice.service

import com.z.userservice.dao.UserDao
import com.z.userservice.domain.User
import com.z.userservice.domain.event.UserEvent
import com.z.userservice.domain.event.UserEventType
import com.z.userservice.domain.event.UserEventType.SAVE
import com.z.userservice.dto.AddUserRequest
import com.z.userservice.dto.DeleteUserRequest
import com.z.userservice.dto.UpdateUserRequest
import com.z.userservice.dto.UserResponse
import com.z.userservice.transformer.AddUserRequestTransformer
import com.z.userservice.transformer.UserDetailsTransformer
import com.z.userservice.transformer.UserTransformer
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityNotFoundException
import javax.validation.ConstraintViolation
import javax.validation.ConstraintViolationException
import javax.validation.Validator

@Service
class UserService(private val userDao: UserDao,
				  private val addUserRequestTransformer: AddUserRequestTransformer,
				  private val userDetailsTransformer: UserDetailsTransformer,
				  private val userTransformer: UserTransformer,
				  private val passwordEncoder: PasswordEncoder,
				  private val applicationEventPublisher: ApplicationEventPublisher,
				  private val validator: Validator): UserDetailsService {

    override fun loadUserByUsername(username:String): UserDetails {
		val user = this.userDao.findByName(username)
						       .orElseThrow { UsernameNotFoundException("user $username not found")}
		return this.userDetailsTransformer.transform(user)
	}

    fun findById(id:Long): UserResponse {
		return this.userTransformer.transform(this.findUserById(id))
	}

	fun findAllByUsernameOrEmail(pageable: Pageable, usernameOrEmail:String): Page<UserResponse> {
		return this.userDao.findAllByNameOrEmailContaining(usernameOrEmail, pageable)
				.map { userTransformer.transform(it) }
	}

	fun findAll(pageable: Pageable): Page<UserResponse> {
		return this.userDao.findAll(pageable).map { userTransformer.transform(it) }
	}

	@Transactional
	fun save(addUserRequest: AddUserRequest): UserResponse {
		validate(addUserRequest)
		val user = this.addUserRequestTransformer.transform(addUserRequest).apply {
			this@UserService.encodeUserPasswordIfNotBlank(this)
		}

		return saveOrUpdate(user).apply {
			applicationEventPublisher.publishEvent(UserEvent(type = SAVE, data = this))
		}
	}

	@Transactional
	fun update(updateUserRequest: UpdateUserRequest): UserResponse {
		validate(updateUserRequest)
		val user = this.findUserById(updateUserRequest.id).apply {
			state = updateUserRequest.state
			if(updateUserRequest.roles.isNotEmpty()){
				roles.apply {//to avoid the delete all query
					removeIf { !updateUserRequest.roles.contains(it) }//remove those the request not contains
					addAll(updateUserRequest.roles.filterNot { roles.contains(it) })//add those the original not contains
				}
			}
			if(updateUserRequest.password != null) {
				password = updateUserRequest.password!!
				this@UserService.encodeUserPasswordIfNotBlank(this)
			}
		}
		return this.saveOrUpdate(user)
	}

	fun delete(deleteUserRequest: DeleteUserRequest): UserResponse {
		return this.update(UpdateUserRequest(id = deleteUserRequest.id, state = false))
	}

	fun existsByName(name:String) = this.userDao.existsByName(name)

	private fun saveOrUpdate(user: User): UserResponse {
		return userTransformer.transform(userDao.save(user))
	}

	private fun encodeUserPasswordIfNotBlank(user: User) {
		if(user.password.isNotBlank()) {
			user.password = passwordEncoder.encode(user.password)
		}
	}

	private fun findUserById(id:Long): User {
		return this.userDao.findById(id).orElseThrow { EntityNotFoundException() }
	}

	@Throws(ConstraintViolationException::class)
	private fun <T> validate(obj: T) {
		val result: Set<ConstraintViolation<T>> = validator.validate(obj)
		if (result.isNotEmpty()) {
			throw ConstraintViolationException(result)
		}
	}
}
