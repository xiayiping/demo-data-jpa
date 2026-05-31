package org.xyp.todoapp.domain.user

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonValue
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.xyp.todoapp.core.enums.ActiveStatus
import java.time.LocalDateTime

@Entity
@Table(name = "todoapp_user")
class User(
    @EmbeddedId
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    val id: UserId,
    var username: String? = null,
    var password: String? = null,
    @Enumerated(jakarta.persistence.EnumType.STRING)
    var status: ActiveStatus? = null,

    @CreationTimestamp
    var createdAt: LocalDateTime? = null,
    @UpdateTimestamp
    var updatedAt: LocalDateTime? = null,

    @Version
    var optimisticVersion: Long? = null,
) {
    companion object {
        const val KEY_ID = "org.xyp.project.todoapp.entity.User"
    }

    constructor(uid: UserId) : this(id = uid, optimisticVersion = null)
}

@Embeddable
data class UserId(
    @JsonValue
    val id: Long
)