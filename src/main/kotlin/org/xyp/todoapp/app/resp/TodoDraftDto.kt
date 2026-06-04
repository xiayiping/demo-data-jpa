package org.xyp.todoapp.app.resp

import com.fasterxml.jackson.annotation.JsonFormat
import org.xyp.todoapp.core.enums.ActiveStatus
import org.xyp.todoapp.domain.todolist.TodoDraft
import org.xyp.todoapp.domain.todolist.TodoDraftId
import org.xyp.todoapp.domain.todolist.TodoTaskPriority
import java.time.LocalDateTime

data class TodoDraftDto(
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    val id: TodoDraftId,
    val title: String,
    val content: String,
    val status: ActiveStatus,

    val startAt: LocalDateTime,

    val dueAt: LocalDateTime,

    val priority: TodoTaskPriority,

    val assignerId: String,
    val assignerType: String,
    val assigneeId: String,
    val assigneeType: String,

    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,

    ) {
    constructor(draft: TodoDraft) : this(
        draft.id,
        draft.title,
        draft.content,
        draft.status,
        draft.startAt,
        draft.dueAt,
        draft.priority,

        draft.assignerId,
        draft.assignerType,
        draft.assigneeId,
        draft.assigneeType,

        draft.createdAt,
        draft.updatedAt,

        )
}
