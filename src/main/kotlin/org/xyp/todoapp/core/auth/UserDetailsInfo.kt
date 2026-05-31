package org.xyp.todoapp.core.auth

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.xyp.todoapp.domain.user.Role
import org.xyp.todoapp.domain.user.UserId

data class UserDetailsInfo(
    val id: UserId,
    private val _username: String,
    private val _password: String,
    val roles: List<Role>
) : UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority> {
        return roles.map { SimpleGrantedAuthority("ROLE_" + it.name) }
    }

    override fun getPassword(): String {
        return _password
    }

    override fun getUsername(): String {
        return _username
    }
}