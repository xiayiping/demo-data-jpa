package org.xyp.project.todoapp.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import org.xyp.project.todoapp.core.enums.ActiveStatus;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "todoapp_user")
public class User {
    @EmbeddedId
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    UserId id;
    String username;
    String password;
    @Enumerated(EnumType.STRING)
    ActiveStatus status;
    Long optimisticVersion;

    public static final String KEY_ID = "org.xyp.project.todoapp.entity.User";

    public static User fromId(UserId id) {
        User user = new User();
        user.id = id;
        return user;
    }

    @Transient
    public Set<Role> roles = new HashSet<>();

    public User addRoleTrans(Role role) {
        roles.add(role);
        return this;
    }

    public User addRolesTrans(Collection<Role> roles) {
        this.roles.addAll(roles);
        return this;
    }
}
