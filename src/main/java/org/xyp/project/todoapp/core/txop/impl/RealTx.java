package org.xyp.project.todoapp.core.txop.impl;

import java.util.function.Supplier;

public interface RealTx {


    <T> T doReturnInTx(Supplier<T> supplier);

    <T> T doReturnInNewTx(Supplier<T> supplier);

    void doRunInTx(Runnable runner);

    void doRunInNewTx(Runnable runner);
}
