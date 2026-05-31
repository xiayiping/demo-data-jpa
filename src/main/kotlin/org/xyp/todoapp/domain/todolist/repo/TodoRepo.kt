package org.xyp.todoapp.domain.todolist.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.xyp.todoapp.domain.todolist.Todo
import org.xyp.todoapp.domain.todolist.TodoId

@Repository
interface TodoRepo : JpaRepository<Todo, TodoId> {
}