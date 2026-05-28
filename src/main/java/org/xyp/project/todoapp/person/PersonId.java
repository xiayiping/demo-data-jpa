package org.xyp.project.todoapp.person;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Embeddable;

@Embeddable
public record PersonId(
  @JsonValue
  Long id
) {
}
