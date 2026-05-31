package org.xyp.todoapp.domain.user.cmd

import com.fasterxml.jackson.annotation.JsonInclude
import org.xyp.todoapp.core.optfield.OptField

data class UpdateUser(
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val username: OptField<String> = OptField.absent(),
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val password: OptField<String> = OptField.absent(),
) {
}