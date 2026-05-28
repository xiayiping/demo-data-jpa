package org.xyp.project.todoapp.infra.idgen.service;

import jakarta.annotation.Nonnull;

import java.util.List;

public interface IdGenerator<T> {
    @Nonnull
    T generate(@Nonnull String keyName);

    @Nonnull
    List<T> generate(@Nonnull String keyName, long size);
}
