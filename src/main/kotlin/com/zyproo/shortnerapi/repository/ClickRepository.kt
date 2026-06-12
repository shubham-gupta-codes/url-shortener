package com.zyproo.shortnerapi.repository

import com.zyproo.shortnerapi.model.Click
import com.zyproo.shortnerapi.model.Url
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface ClickRepository: JpaRepository<Click, Long>{
    fun findByUrl(url: Url): List<Click>
    fun countByUrl(url: Url): Long
    fun findByUrlAndClickedAtBetween(
        url: Url,
        start: LocalDateTime,
        end:LocalDateTime
    ): List<Click>

    fun findByUrlAndDeviceType(url: Url, deviceType: String): List<Click>
}