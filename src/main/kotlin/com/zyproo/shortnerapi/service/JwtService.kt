package com.zyproo.shortnerapi.service

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.Date
import javax.crypto.SecretKey

@Service
class JwtService{
    @Value("\${app.jwt.secret}")
    private lateinit var jwtSecret: String

    @Value("\${app.jwt.expiration}")
    private var expiration: Long = 0

    private fun getSignInKey(): SecretKey{
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret))
    }

    fun generateToken(userDetails: UserDetails):String{
        return Jwts.builder()
            .subject(userDetails.username)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(Date(System.currentTimeMillis() + expiration))
            .signWith( getSignInKey())
            .compact()
    }
    fun isTokenValid(token: String,userDetails:UserDetails): Boolean {
        val username = extractUsername(token)
        val isExpired = isTokenExpired(token)
        return username == userDetails.username && !isExpired
    }
    fun isTokenExpired(token:String): Boolean{
        return Jwts.parser()
            .verifyWith(getSignInKey())
            .build()
            .parseSignedClaims(token)
            .payload
            .expiration
            .before(Date())
    }
    fun extractUsername(token: String): String{
        return Jwts.parser()
            .verifyWith(getSignInKey())
            .build()
            .parseSignedClaims(token)
            .payload
            .subject
    }







}