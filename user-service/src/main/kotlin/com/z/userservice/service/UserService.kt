package com.z.userservice.service

import com.z.userservice.dao.UserDao
import com.z.userservice.domain.User
import com.z.userservice.dto.AddUserRequest
import com.z.userservice.dto.DeleteUserRequest
import com.z.userservice.dto.UpdateUserRequest
import com.z.userservice.dto.UserResponse
import com.z.userservice.transformer.AddUserRequestTransformer
import com.z.userservice.transformer.UserDetailsTransformer
import com.z.userservice.transformer.UserTransformer
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityNotFoundException

@Service
class UserService(private val userDao: UserDao,
				  private val addUserRequestTransformer: AddUserRequestTransformer,
				  private val userDetailsTransformer: UserDetailsTransformer,
				  private val userTransformer: UserTransformer,
				  private val passwordEncoder: PasswordEncoder): UserDetailsService {

    override fun loadUserByUsername(username:String): UserDetails {
		val user = this.userDao.findByName(username)
						       .orElseThrow { UsernameNotFoundException("user $username not found")}
		return this.userDetailsTransformer.transform(user)
	}

    fun findById(id:Long): UserResponse {
		return this.userTransformer.transform(this.findUserById(id))
	}

	fun findAll(pageable: Pageable): Page<UserResponse> {
		return this.userDao.findAll(pageable).map { userTransformer.transform(it) }
	}

	@Transactional
	fun save(addUserRequest: AddUserRequest): UserResponse {
		val user = this.addUserRequestTransformer.transform(addUserRequest).apply {
			this@UserService.encodeUserPasswordIfNotBlank(this)
		}
		return saveOrUpdate(user)
	}

	@Transactional
	fun update(updateUserRequest: UpdateUserRequest): UserResponse {
		val user = this.findUserById(updateUserRequest.id).apply {
			state = updateUserRequest.state
			if(roles.isNotEmpty()){
				roles.apply {//to avoid the delete all query
					removeIf { !updateUserRequest.roles.contains(it) }
					addAll(updateUserRequest.roles)
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
		return this.update(UpdateUserRequest(id = deleteUserRequest.id))
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
}
