package org.xyp.todoapp.domain.todolist.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.xyp.todoapp.domain.todolist.TodoDraft
import org.xyp.todoapp.domain.todolist.TodoDraftId

@Repository
interface TodoDraftRepo : JpaRepository<TodoDraft, TodoDraftId> {
}