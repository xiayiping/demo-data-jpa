package org.xyp.todoapp.app

import org.springframework.stereotype.Service
import org.xyp.todoapp.app.resp.TodoDraftDto
import org.xyp.todoapp.core.txop.TransactionalOp
import org.xyp.todoapp.domain.todolist.TodoService
import org.xyp.todoapp.domain.todolist.cmd.CreateDraftCmd

@Service
class TodoApp(
    val txOp: TransactionalOp,
    val todoService: TodoService
) {
    fun createDraft(cmd: CreateDraftCmd, requester: String): TodoDraftDto {
        val draft = todoService.createDraft(cmd, requester)
        return TodoDraftDto(draft)
    }
}