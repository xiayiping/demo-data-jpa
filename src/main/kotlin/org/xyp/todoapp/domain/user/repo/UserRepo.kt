package org.xyp.todoapp.domain.user.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.xyp.todoapp.domain.user.User
import org.xyp.todoapp.domain.user.UserId

@Repository
interface UserRepo : JpaRepository<User, UserId> {
    fun findByUsername(username: String): User?
}