package org.xyp.project.todoapp.core.idgen.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Embeddable;

@Embeddable
public record IdTableId(
  @JsonValue
  String id
) {
}
