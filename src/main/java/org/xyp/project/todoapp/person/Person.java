package org.xyp.project.todoapp.person;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "todos_person")
public class Person {
    @EmbeddedId
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    PersonId id;

    String username;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
    @JoinColumn(name = "p_id")
    List<Address> addresses = new ArrayList<>();

    @Nullable
    @Version
    Long optimisticVersion = null;

    public Person addAddress(Address address) {
//        address.setPersonId(this.getId());
        this.addresses.add(address);
        return this;
    }
}
