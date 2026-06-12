package com.zyproo.shortnerapi.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "urls")
class Url(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    val id:Long = 0,
    val originalUrl:String,
    val shortUrl:String,
    val clickCount: Long = 0,
    @ManyToOne
    @JoinColumn(name = "url_id")
    val user:User,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val expiresAt: LocalDateTime? = null
)