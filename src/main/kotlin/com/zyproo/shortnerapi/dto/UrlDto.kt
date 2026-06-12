package com.zyproo.shortnerapi.dto

import java.time.LocalDateTime

data class UrlDto(
    val shortUrl: String,
    val originalUrl: String,
    val clickCount: Long,
    val createdAt: LocalDateTime,
    val expiresAt: LocalDateTime?
)