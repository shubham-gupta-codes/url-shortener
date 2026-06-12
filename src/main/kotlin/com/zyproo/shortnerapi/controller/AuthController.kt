package com.zyproo.shortnerapi.controller

import com.zyproo.shortnerapi.dto.UserRequest
import com.zyproo.shortnerapi.dto.UserResponse
import com.zyproo.shortnerapi.model.User
import com.zyproo.shortnerapi.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val userService: UserService
){
    @PostMapping("/register")
    fun register(@RequestBody userRequest: UserRequest): ResponseEntity<User> {
        return ResponseEntity(userService.register(userRequest), HttpStatus.CREATED)
    }

    @PostMapping("/login")
        fun login(@RequestBody request: UserRequest): ResponseEntity<UserResponse>{
            return ResponseEntity(userService.login(request), HttpStatus.OK)
        }
}