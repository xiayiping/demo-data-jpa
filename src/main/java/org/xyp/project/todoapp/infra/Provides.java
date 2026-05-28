package org.xyp.project.todoapp.infra;

import java.util.function.Supplier;

public class Provides {
    private Provides() {
    }

    public static <T> T from(Supplier<T> supplier) {
        return supplier.get();
    }
}
