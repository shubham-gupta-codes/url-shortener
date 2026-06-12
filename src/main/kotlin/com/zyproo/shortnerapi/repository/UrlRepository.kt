package com.zyproo.shortnerapi.repository

import com.zyproo.shortnerapi.model.Url
import com.zyproo.shortnerapi.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UrlRepository: JpaRepository<Url, Long> {

    fun findByShortUrl(shortUrl:String):Url?
    fun findByUser(user: User):List<Url>
}