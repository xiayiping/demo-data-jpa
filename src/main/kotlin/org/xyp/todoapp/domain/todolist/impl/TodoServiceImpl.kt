package org.xyp.todoapp.domain.todolist.impl

import org.springframework.stereotype.Service
import org.xyp.todoapp.core.enums.ActiveStatus
import org.xyp.todoapp.core.idgen.IdGenerator
import org.xyp.todoapp.core.txop.TransactionalOp
import org.xyp.todoapp.domain.todolist.TodoConst
import org.xyp.todoapp.domain.todolist.TodoDraft
import org.xyp.todoapp.domain.todolist.TodoDraftId
import org.xyp.todoapp.domain.todolist.TodoService
import org.xyp.todoapp.domain.todolist.cmd.CreateDraftCmd
import org.xyp.todoapp.domain.todolist.repo.TodoClosedRepo
import org.xyp.todoapp.domain.todolist.repo.TodoDraftRepo
import org.xyp.todoapp.domain.todolist.repo.TodoRepo

@Service
class TodoServiceImpl(
    val txOp: TransactionalOp,
    val todoRepo: TodoRepo,
    val todoDraftRepo: TodoDraftRepo,
    val todoClosedRepo: TodoClosedRepo,
    val idGenerator: IdGenerator<Long>
) : TodoService {
    override fun createDraft(cmd: CreateDraftCmd, requester: String): TodoDraft {
        val draft = TodoDraft(
            TodoDraftId.create(idGenerator),
            cmd.title,
            cmd.content,
            ActiveStatus.ACTIVE,
            cmd.startAt.atStartOfDay(),
            cmd.dueAt.atStartOfDay(),
            null,
            cmd.priority,
            requester,
            TodoConst.TYPE_USER,
            requester,
            TodoConst.TYPE_USER,
            null,
            null,
            null,
        )

        return txOp.returnInTx { todoDraftRepo.save(draft) }!!
    }
}