package org.xyp.project.todoapp.core.idgen.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "todoapp_id_table")
public class IdTable {
    @EmbeddedId
    IdTableId id;
    long lastValue;
    long batchSize;
    @Version
    Long optimisticVersion;
    @CreationTimestamp
    LocalDateTime createdAt;
    @UpdateTimestamp
    LocalDateTime updatedAt;

    @Transient
    long lastUsedValue = 0;

    public IdTable(IdTableId id) {
        this.id = id;
    }
}
