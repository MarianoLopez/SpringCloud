package com.z.userservice.resource

import com.z.userservice.dto.DeleteUserRequest
import com.z.userservice.dto.UpdateUserRequest
import com.z.userservice.dto.UserResponse
import com.z.userservice.service.management.*
import com.z.userservice.utils.ResourceConstant.USER_RESOURCE
import com.z.zcoreblocking.utils.swagger.ApiPageableWithAuth
import com.z.zcoreblocking.utils.swagger.AuthorizationHeader
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore

@RestController
@RequestMapping(USER_RESOURCE)
class UserResource(private val userManagementService: UserManagement,
				   private val userSearchService: UserSearch) {

	@ApiPageableWithAuth
    @GetMapping
    fun findAll(@ApiIgnore pageable:Pageable, @RequestParam(required = false) search:String?): ResponseEntity<Page<UserResponse>> {
		return ResponseEntity.ok(this.userSearchService.findAllByUsernameOrEmail(pageable, search ?: ""))
	}

	@AuthorizationHeader
	@GetMapping("/{id}")
	fun findById(@PathVariable id:Long): ResponseEntity<UserResponse> {
		return ResponseEntity.ok(this.userSearchService.findById(id))
	}

	@AuthorizationHeader
	@PutMapping
	fun update(@RequestBody updateUserRequest: UpdateUserRequest): ResponseEntity<UserResponse> {
		return ResponseEntity.ok(this.userManagementService.update(updateUserRequest))
	}

	@AuthorizationHeader
	@DeleteMapping("/{id}")
	fun delete(@PathVariable id:Long): ResponseEntity<UserResponse> {
		return ResponseEntity.ok(this.userManagementService.delete(DeleteUserRequest(id)))
	}
}