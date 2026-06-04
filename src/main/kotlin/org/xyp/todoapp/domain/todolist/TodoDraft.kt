package org.xyp.todoapp.domain.todolist

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonValue
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.xyp.todoapp.core.enums.ActiveStatus
import org.xyp.todoapp.core.idgen.IdGenerator
import java.time.LocalDateTime

@Entity
@Table(name = "todoapp_todo_draft")
class TodoDraft(
    @EmbeddedId
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    val id: TodoDraftId,

    var title: String,

    var content: String,

    @Enumerated(EnumType.STRING)
    var status: ActiveStatus = ActiveStatus.ACTIVE,

    var startAt: LocalDateTime,

    var dueAt: LocalDateTime,

    var finishedAt: LocalDateTime?,

    @Convert(converter = TodoTaskPriorityConverter::class)
    var priority: TodoTaskPriority = TodoTaskPriority.MEDIUM,

    val assignerId: String,
    val assignerType: String,
    val assigneeId: String,
    val assigneeType: String,

    @CreationTimestamp
    var createdAt: LocalDateTime? = null,
    @UpdateTimestamp
    var updatedAt: LocalDateTime? = null,

    @Version
    var optimisticVersion: Long? = null,
) {
    companion object {
    }
}

@Embeddable
data class TodoDraftId(
    @JsonValue
    val id: Long
) {
    companion object {

        private const val ID_KEY: String = "org.xyp.todoapp.domain.todolist.TodoDraft"

        fun create(idGenerator: IdGenerator<Long>): TodoDraftId {
            return TodoDraftId(idGenerator.generate(ID_KEY))
        }
    }
}