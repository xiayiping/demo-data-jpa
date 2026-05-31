package org.xyp.todoapp.app.resp

import com.fasterxml.jackson.annotation.JsonFormat
import org.xyp.todoapp.domain.user.RoleId

data class RoleDto(
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    val id: RoleId,
    val name: String,
)
