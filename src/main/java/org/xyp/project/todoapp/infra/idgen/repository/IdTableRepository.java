package org.xyp.project.todoapp.infra.idgen.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.xyp.project.todoapp.infra.idgen.entity.IdTable;
import org.xyp.project.todoapp.infra.idgen.entity.IdTableId;

import java.util.Optional;

@Repository
public interface IdTableRepository extends JpaRepository<IdTable, IdTableId> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select t from IdTable t where t.id = :id ")
    Optional<IdTable> findByIdLocked(IdTableId id);
}
