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
@Table(name = "clicks")
class Click(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    val id:Long=0,
    val deviceType:String? = null,
    val clickedAt: LocalDateTime = LocalDateTime.now(),
    val ipAddress:String? = null,
    @ManyToOne
    @JoinColumn(name = "url_id")
    val url: Url
)