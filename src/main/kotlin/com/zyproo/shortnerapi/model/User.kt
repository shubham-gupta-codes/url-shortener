package com.zyproo.shortnerapi.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.constraints.NotBlank
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime

@Entity
@Table(name = "users")
class User(
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Id
    val id:Long = 0,
    @Column(unique = true, nullable = false)
    @field:NotBlank(message = "Name is required")
    private val username:String = "",
    @field:NotBlank(message = "password is required")
    private val password:String = "",
    private val role: Role = Role.USER,
    val createdAt: LocalDateTime = LocalDateTime.now()
): UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority> = listOf(SimpleGrantedAuthority(role.name))
    override fun getPassword(): String = password
    override fun getUsername(): String = username
    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = true
}
enum class Role {
    USER,
    ADMIN
}