package com.zyproo.shortnerapi.repository

import com.zyproo.shortnerapi.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, Long> {
    fun findByUsername(username:String):User?
}