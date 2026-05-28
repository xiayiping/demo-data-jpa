package org.xyp.project.todoapp.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Embeddable;

@Embeddable
public record UserRoleRelId(
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  UserId userId,

  @JsonFormat(shape = JsonFormat.Shape.STRING)
  RoleId roleId
) {
}
