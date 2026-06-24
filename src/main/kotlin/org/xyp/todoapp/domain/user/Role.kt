package org.xyp.todoapp.domain.user

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonValue
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.xyp.todoapp.core.enums.ActiveStatus
import java.time.LocalDateTime

@Entity
@Table(name = "todoapp_role")
class Role(

    @EmbeddedId
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    val id: RoleId,

    var name: String? = null,

    @Enumerated(EnumType.STRING)
    var status: ActiveStatus? = null,

    @CreationTimestamp
    var createdAt: LocalDateTime? = null,
    @UpdateTimestamp
    var updatedAt: LocalDateTime? = null,

    @Version
    var optimisticVersion: Long? = null,
) {
    constructor(rid: RoleId) : this(id = rid, optimisticVersion = null)
}
@Embeddable
data class RoleId(
    @JsonValue
    val id: Long
)