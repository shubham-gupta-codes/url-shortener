package com.zyproo.shortnerapi.service

import com.zyproo.shortnerapi.model.Click
import com.zyproo.shortnerapi.repository.ClickRepository
import com.zyproo.shortnerapi.repository.UrlRepository
import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date

@Service
class ClickService(
    private val clickRepository: ClickRepository,
    private val urlRepository: UrlRepository
){
    fun saveClick(shortCode:String,request: HttpServletRequest) {
        val url = urlRepository.findByShortUrl(shortCode) ?: throw IllegalArgumentException("shortCode $shortCode not found")
        val userAgent = request.getHeader("User-Agent") ?: ""
        val deviceType = if(userAgent.contains("Mobile")) "Mobile" else "desktop"
        clickRepository.save(
            Click(
                deviceType = deviceType,ipAddress = request.remoteAddr, url = url))
    }

    fun getTotalClicks(shortCode:String):Long{
        val findUrl = urlRepository.findByShortUrl(shortCode) ?: throw IllegalArgumentException("shortCode $shortCode not found")
        return clickRepository.countByUrl(findUrl)
    }

    fun getCountByDevice(shortCode:String): Map<String,Long>{
        val findUrl = urlRepository.findByShortUrl(shortCode) ?: throw IllegalArgumentException("shortCode $shortCode not found")
        val clicks = clickRepository.findByUrl(findUrl)
        val result = clicks
            .groupBy{it.deviceType ?: "Unknown"}
            .mapValues { it.value.size.toLong() }
        return result
    }
    fun getClickByDates(shortCode:String,startDate: LocalDateTime,endDate: LocalDateTime):Map<LocalDate, Long>{
        val findUrl = urlRepository.findByShortUrl(shortCode) ?: throw IllegalArgumentException("shortCode $shortCode not found")
        val clicks = clickRepository.findByUrlAndClickedAtBetween(url = findUrl, start = startDate, end = endDate)
        return clicks
            .groupBy{it.clickedAt.toLocalDate()}
        .mapValues { it.value.size.toLong() }
    }
}

