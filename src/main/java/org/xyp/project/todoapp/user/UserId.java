package org.xyp.project.todoapp.user;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Embeddable;

@Embeddable
public record UserId(@JsonValue long id) {
}
