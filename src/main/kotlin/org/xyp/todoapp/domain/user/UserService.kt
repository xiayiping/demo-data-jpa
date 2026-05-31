package org.xyp.todoapp.domain.user

import org.springframework.stereotype.Service
import org.xyp.todoapp.domain.user.repo.UserRepo

@Service
class UserService(
    val userRepo: UserRepo
) {
    fun findByUsername(username: String): User? {
        return userRepo.findByUsername(username)
    }
}