package com.zyproo.shortnerapi.service

import com.zyproo.shortnerapi.dto.UserRequest
import com.zyproo.shortnerapi.model.User
import com.zyproo.shortnerapi.repository.UserRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.Optional

@ExtendWith(MockKExtension::class)
class UserServiceTest {
    @MockK
    private lateinit var userRepository: UserRepository

    @MockK
    private lateinit var authManager: AuthenticationManager

    @MockK
    private lateinit var jwtService: JwtService

    @MockK
    private lateinit var passwordEncoder: PasswordEncoder

    @InjectMockKs
    private lateinit var userService: UserService

    @Test
    fun `should return user after register successfully`(){
        val userRequest = UserRequest("shubham","1234")
        val encodedPassword = "encoded123"
        val user = User(username = "shubham", password = encodedPassword)

        //then
        every { userRepository.findByUsername(userRequest.username) } returns null
        every { passwordEncoder.encode("1234") } returns encodedPassword
        every { userRepository.save(any()) } returns user
        //when
        val result = userService.register(userRequest)

        assertEquals("shubham", result.username)

        verify(exactly = 1) { userRepository.save(any()) }
        verify(exactly = 1) { passwordEncoder.encode("1234")}
    }

    @Test
    fun `should throw user already exists if username already exists`(){
        val userRequest = UserRequest("shubham","1234")
        val user = User(username = "shubham", password = "encoded1234")
        every { userRepository.findByUsername(any()) } returns user

        assertThrows<IllegalArgumentException>{
            userService.register(userRequest)
        }

        verify(exactly = 0){userRepository.save(any())}
    }

    @Test
    fun `should return token if login success`(){
        val userRequest = UserRequest("shubham","1234")
        val user = User(username = "shubham", password = "encoded1234")
        every { authManager.authenticate(any())} returns mockk()
        every { userRepository.findByUsername(any()) } returns user
        every { jwtService.generateToken(user) } returns "token"
        val result = userService.login(userRequest)

        assertEquals("token", result.token)

        verify(exactly = 1) { jwtService.generateToken(any()) }
        verify(exactly = 1) { authManager.authenticate(any()) }
    }

    @Test
    fun `should throw user authentication exception`(){
        val userRequest = UserRequest("shubham","1234")
        every { authManager.authenticate(any()) } throws BadCredentialsException("Invalid Credentials")
        assertThrows<BadCredentialsException>{
            userService.login(userRequest)
        }

        verify(exactly = 1){authManager.authenticate(any())}
        verify(exactly = 0){jwtService.generateToken(any())}
    }
}