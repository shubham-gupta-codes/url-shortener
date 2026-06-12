package com.zyproo.shortnerapi.dto

import jakarta.validation.constraints.NotBlank

data class UserRequest(
    @field:NotBlank
    val username: String,
    @field:NotBlank
    val password:String
)