package com.z.userservice

import com.z.userservice.service.UserService
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort

@SpringBootApplication
class UserServiceApplication(private val userService: UserService): ApplicationRunner {
	override fun run(args: ApplicationArguments?) {
		val result = userService.findAllByUsernameOrEmail(
				PageRequest.of(0, 5, Sort.by("name"))
				,"villa")
		println(result)
	}
}

fun main(args: Array<String>) {
	runApplication<UserServiceApplication>(*args)
}
