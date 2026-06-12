package com.zyproo.shortnerapi.service


import com.zyproo.shortnerapi.dto.UserRequest
import com.zyproo.shortnerapi.dto.UserResponse
import com.zyproo.shortnerapi.model.User
import com.zyproo.shortnerapi.repository.UserRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val authManager: AuthenticationManager
){

    fun register(user: UserRequest): User {
        if(userRepository.findByUsername(user.username) != null) {
            throw IllegalArgumentException("User already exists")
        }
        return userRepository.save(
            User(
                username = user.username,
                password = passwordEncoder.encode(user.password)
                    ?: throw IllegalArgumentException("Encoding Failed")
            )
        )
    }

    fun login(user:UserRequest): UserResponse {
        authManager.authenticate(
            UsernamePasswordAuthenticationToken(
                user.username,
                user.password
            )
        )
        val foundUser = userRepository.findByUsername(user.username)
        ?: throw IllegalArgumentException("User not found")
        return UserResponse(jwtService.generateToken(foundUser))
    }
}