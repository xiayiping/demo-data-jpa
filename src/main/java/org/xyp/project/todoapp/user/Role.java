package org.xyp.project.todoapp.user;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import org.xyp.project.todoapp.shared.enums.ActiveStatus;

@Data
@Entity
@Table(name = "todoapp_role")
public class Role {
    @EmbeddedId
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    RoleId id;
    String name;
    @Enumerated(EnumType.STRING)
    ActiveStatus status;
    @Version
    Long optimisticVersion;
}
