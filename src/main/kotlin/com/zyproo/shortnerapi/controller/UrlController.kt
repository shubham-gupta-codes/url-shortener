package com.zyproo.shortnerapi.controller


import com.zyproo.shortnerapi.dto.UrlDto
import com.zyproo.shortnerapi.dto.UrlRequest
import com.zyproo.shortnerapi.dto.UrlResponse
import com.zyproo.shortnerapi.service.ClickService
import com.zyproo.shortnerapi.service.UrlService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/url")
class UrlController(
    private val urlService: UrlService,
    private val clickService: ClickService
) {
    @PostMapping("/shorten")
    fun shortenUrl(@RequestBody urlRequest: UrlRequest): ResponseEntity<UrlResponse> {
        val username = SecurityContextHolder
            .getContext().authentication?.name ?: throw IllegalStateException("Username is null")
        return ResponseEntity(UrlResponse(shortUrl = urlService.generateShortUrl(username,urlRequest.url)), HttpStatus.CREATED)
    }

    @GetMapping("/urls")
    fun getUrls():ResponseEntity<List<UrlDto>>{
        val username = SecurityContextHolder.getContext().authentication?.name ?: throw IllegalStateException("Username is null")
        return ResponseEntity.ok(urlService.getUrlByUsername(username))
    }
    @DeleteMapping("/{shortCode}")
    fun deleteUrl(@PathVariable("shortCode") shortCode: String): ResponseEntity<String> {
        urlService.deleteUrl(shortCode)
        return ResponseEntity("Url Deleted Successfully",HttpStatus.OK)
    }

    @GetMapping("/{shortCode}/analytics/devices")
    fun getClicksByDevice(@PathVariable("shortCode") shortCode: String):ResponseEntity<Map<String, Long>> {
        val device = clickService.getCountByDevice(shortCode)
        return ResponseEntity(device,HttpStatus.OK)
    }
    @GetMapping("/{shortCode}/analytics/dates")
    fun getClicksByDate(@PathVariable("shortCode") shortCode: String, @RequestParam startDate: LocalDateTime,@RequestParam endDate: LocalDateTime):ResponseEntity<Map<LocalDate, Long>> {
        val count = clickService.getClickByDates(shortCode,startDate,endDate)
        return ResponseEntity(count, HttpStatus.OK)
    }
}