package org.xyp.todoapp.domain.todolist

import org.xyp.todoapp.domain.todolist.cmd.CreateDraftCmd

interface TodoService {
    fun createDraft(cmd: CreateDraftCmd, requester: String)
}