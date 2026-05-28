package org.xyp.project.todoapp.person;

import jakarta.persistence.*;
import lombok.Data;
import org.jspecify.annotations.Nullable;

@Data
@Entity
@Table(name = "todos_address")
public class Address {
    @EmbeddedId
    AddressId id;

    String street;

    @Embedded
    @AttributeOverrides({
      @AttributeOverride(name = "id", column = @Column(name = "p_id"))
    })
    PersonId personId;

    @Nullable
    @Version
    Long optimisticVersion;

}
