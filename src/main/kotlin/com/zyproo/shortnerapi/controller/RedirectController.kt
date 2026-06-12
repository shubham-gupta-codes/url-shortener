package com.zyproo.shortnerapi.controller


import com.zyproo.shortnerapi.dto.UrlResponse
import com.zyproo.shortnerapi.service.ClickService
import com.zyproo.shortnerapi.service.UrlService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping
class RedirectController(
    private val urlService: UrlService,
    private val clickService: ClickService,
) {
    @GetMapping("/{shortcode}")
    fun redirect(@PathVariable shortcode: String,httpServletRequest: HttpServletRequest): ResponseEntity<String> {
        val url = urlService.getUrl(shortcode)
        clickService.saveClick(shortcode,httpServletRequest)
        return ResponseEntity
            .status(HttpStatus.FOUND)
            .header("Location",url)
            .build()

    }
}