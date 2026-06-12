package com.zyproo.shortnerapi.service

import com.zyproo.shortnerapi.dto.UrlRequest
import com.zyproo.shortnerapi.model.Url
import com.zyproo.shortnerapi.model.User
import com.zyproo.shortnerapi.repository.UrlRepository
import com.zyproo.shortnerapi.repository.UserRepository
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ValueOperations

@ExtendWith(MockKExtension::class)
class UrlServiceTest{
    @MockK
    private lateinit var urlRepository: UrlRepository

    @MockK
    private lateinit var userRepository: UserRepository

    @MockK
    private lateinit var redisTemplate: RedisTemplate<String, String>

    @MockK
    private lateinit var valueOperations: ValueOperations<String, String>

    @InjectMockKs
    private lateinit var urlService: UrlService

    @Test
    fun `should return short ulr for long url`(){
        val url = UrlRequest("https://google.com")
        val user = User(username = "shubham",password = "encodedpassword")
        val findUrl = Url(originalUrl = "https://google.com", shortUrl = "shorturl", user = user )
        every { userRepository.findByUsername(any()) } returns user
        every { urlRepository.findByShortUrl(any()) } returns null
        every { urlRepository.save(any()) } returns findUrl
        every { redisTemplate.opsForValue() } returns valueOperations
        every { valueOperations.set(any(), any(), any(), any()) } just Runs

        val result = urlService.generateShortUrl("shubham",url.url)

        assertThat(result).isNotEmpty
        verify(exactly = 1) { urlRepository.save(any()) }
        verify(exactly = 1) { valueOperations.set(any(), any(), any(), any()) }

    }

    @Test
    fun `should return url if shortcode is valid when redis miss`(){
        val user = User(username = "shubham",password = "encodedpassword")
        val url = Url(originalUrl = "https://google.com", shortUrl = "shorturl", user = user )
        every{ redisTemplate.opsForValue() } returns valueOperations
        every{ valueOperations.get("shorturl") } returns null
        every { urlRepository.findByShortUrl(any()) } returns url
        every { valueOperations.set(any(), any(), any(), any()) } just Runs

        val result = urlService.getUrl("shorturl")

        assertEquals("https://google.com", result)
    }

    @Test
    fun `should return url if shortcode is valid and redis miss`(){
        every{ redisTemplate.opsForValue() } returns valueOperations
        every{ valueOperations.get("shorturl") } returns "https://google.com"

        val result = urlService.getUrl("shorturl")

        assertEquals("https://google.com", result)
        verify(exactly = 0) { urlRepository.findByShortUrl(any()) }

    }
}