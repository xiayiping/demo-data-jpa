package org.xyp.todoapp.domain.user.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.xyp.todoapp.domain.user.Role
import org.xyp.todoapp.domain.user.RoleId

@Repository
interface RoleRepo : JpaRepository<Role, RoleId> {
}