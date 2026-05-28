package org.xyp.project.todoapp.infra.idgen.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Embeddable;

@Embeddable
public record IdTableId(
  @JsonValue
  String id
) {
}
