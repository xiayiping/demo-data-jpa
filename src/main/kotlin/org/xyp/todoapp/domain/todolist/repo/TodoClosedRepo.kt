package org.xyp.todoapp.domain.todolist.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.xyp.todoapp.domain.todolist.TodoClosed
import org.xyp.todoapp.domain.todolist.TodoClosedId

@Repository
interface TodoClosedRepo : JpaRepository<TodoClosed, TodoClosedId> {
}