package org.xyp.project.todoapp.core.txop;

import java.util.function.Supplier;

public interface TransactionalOp {
    <T> T returnInTx(Supplier<T> supplier);

    <T> T returnInNewTx(Supplier<T> supplier);

    void runInTx(Runnable runner);

    void runInNewTx(Runnable runner);
}
