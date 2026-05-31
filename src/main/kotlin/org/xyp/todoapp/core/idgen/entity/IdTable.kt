package org.xyp.todoapp.core.idgen.entity

import com.fasterxml.jackson.annotation.JsonValue
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "todoapp_id_table")
class IdTable(

    @EmbeddedId
    val id: IdTableId,

    var lastValue: Long = 0,

    var batchSize: Long = 0,

    @jakarta.persistence.Version
    var optimisticVersion: Long? = null,

    @CreationTimestamp
    var createdAt: LocalDateTime? = null,

    @UpdateTimestamp
    var updatedAt: LocalDateTime? = null,

    @Transient
    var lastUsedValue: Long = 0

) {
    constructor(id: IdTableId) : this(id = id, lastValue = 0)
}

@Embeddable
data class IdTableId(
    @JsonValue
    val id: String
)
