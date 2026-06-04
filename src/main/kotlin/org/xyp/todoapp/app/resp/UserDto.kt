package org.xyp.todoapp.app.resp

import com.fasterxml.jackson.annotation.JsonInclude
import org.xyp.todoapp.core.enums.ActiveStatus
import org.xyp.todoapp.core.json.MaskedData
import org.xyp.todoapp.core.json.OptField
import org.xyp.todoapp.domain.user.UserId
import java.time.LocalDateTime

data class UserDto(

    var id: UserId? = null,
    var username: String? = null,
    
    @MaskedData
    var password: String? = null,
    var createdAt: LocalDateTime? = null,
    var updatedAt: LocalDateTime? = null,
    var activeStatus: ActiveStatus? = null,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    var friend: OptField<UserDtoJava?>? = null
) {
}