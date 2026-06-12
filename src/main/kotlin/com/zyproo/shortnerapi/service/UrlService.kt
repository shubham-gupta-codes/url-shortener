package com.zyproo.shortnerapi.service


import com.zyproo.shortnerapi.dto.UrlDto
import com.zyproo.shortnerapi.model.Url
import com.zyproo.shortnerapi.repository.UrlRepository
import com.zyproo.shortnerapi.repository.UserRepository
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class UrlService(
    private val urlRepository: UrlRepository,
    private val redisTemplate: RedisTemplate<String, String>,
    private val userRepository: UserRepository
){
    fun generateShortUrl(username:String,longUrl:String):String{
        val user = userRepository.findByUsername(username) ?: throw IllegalArgumentException("username not found")
        val findUrl = urlRepository.save(Url(originalUrl = longUrl,shortUrl = generateShortCode(), user = user))
        redisTemplate.opsForValue().set(
            findUrl.shortUrl,
            findUrl.originalUrl,
            24,
            TimeUnit.HOURS
        )
        return findUrl.shortUrl
    }
    private fun generateShortCode():String{
        val chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        var code:String
        do {
            code = (1..6).map { chars.random() }.joinToString("")
        }while (urlRepository.findByShortUrl(code) != null)
        return code
    }

    fun getUrl(shortCode:String):String{
        val longUrl = redisTemplate.opsForValue().get(shortCode)
        if(longUrl != null){
            return longUrl
        }else{
            val findUrl = urlRepository.findByShortUrl(shortCode) ?: throw IllegalArgumentException("shortcode not found")
            redisTemplate.opsForValue().set(shortCode, findUrl.originalUrl,24, TimeUnit.HOURS)
            return findUrl.originalUrl
        }
    }

    fun getUrlByUsername(username:String):List<UrlDto>{
        val user = userRepository.findByUsername(username) ?: throw IllegalArgumentException("username not found")
        return urlRepository.findByUser(user).map { url ->
            UrlDto(
                shortUrl = url.shortUrl,
                originalUrl = url.originalUrl,
                clickCount = url.clickCount,
                createdAt = url.createdAt,
                expiresAt = url.expiresAt
            )
        }
    }

    fun deleteUrl(shortCode:String){
        val url = urlRepository.findByShortUrl(shortCode) ?: throw IllegalArgumentException("shortcode not found")
        urlRepository.delete(url)
        redisTemplate.delete(shortCode)
    }
}