package org.xyp.todoapp.core.idgen.repo

import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.xyp.todoapp.core.idgen.entity.IdTable
import org.xyp.todoapp.core.idgen.entity.IdTableId

@Repository
interface IdTableRepo : JpaRepository<IdTable, IdTableId> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select t from IdTable t where t.id = :id ")
    fun findByIdLocked(id: IdTableId): IdTable?
}