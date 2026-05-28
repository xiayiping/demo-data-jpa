package org.xyp.project.todoapp.user;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "todoapp_rel_user_role")
public class UserRoleRel {
    @EmbeddedId
    @AttributeOverrides({
      @AttributeOverride(name = "userId.id", column = @Column(name = "user_id")),
      @AttributeOverride(name = "roleId.id", column = @Column(name = "role_id")),
    })
    UserRoleRelId id;
}
