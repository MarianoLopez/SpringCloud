package com.z.userservice.resource

import com.z.userservice.dto.AddUserRequest
import com.z.userservice.dto.DeleteUserRequest
import com.z.userservice.dto.UpdateUserRequest
import com.z.userservice.dto.UserResponse
import com.z.userservice.service.UserService
import com.z.userservice.utils.ResourceConstant.USER_RESOURCE
import com.z.zcoreblocking.utils.ApiPageable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore

@RestController
@RequestMapping(USER_RESOURCE)
class UserResource(private val userService: UserService) {

	@ApiPageable
    @GetMapping
    fun findAll(@ApiIgnore pageable:Pageable): ResponseEntity<Page<UserResponse>> {
		return ResponseEntity.ok(this.userService.findAll(pageable))
	}

	@GetMapping("/{id}")
	fun findById(@PathVariable id:Long): ResponseEntity<UserResponse> {
		return ResponseEntity.ok(this.userService.findById(id))
	}

	@PostMapping
	fun save(@RequestBody addUserRequest: AddUserRequest): ResponseEntity<UserResponse> {
		return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.save(addUserRequest))
	}

	@PutMapping
	fun update(@RequestBody updateUserRequest: UpdateUserRequest): ResponseEntity<UserResponse> {
		return ResponseEntity.ok(this.userService.update(updateUserRequest))
	}

	@DeleteMapping("/{id}")
	fun delete(@PathVariable id:Long): ResponseEntity<UserResponse> {
		return ResponseEntity.ok(this.userService.delete(DeleteUserRequest(id)))
	}
}