package org.xyp.todoapp.domain.todolist.cmd

import org.xyp.todoapp.domain.todolist.TodoTaskPriority
import java.time.LocalDate

data class CreateDraftCmd(
    val startAt: LocalDate,
    val dueAt: LocalDate,
    val priority: TodoTaskPriority,
    val title: String,
    val content: String,
    var requester: String?,
)

