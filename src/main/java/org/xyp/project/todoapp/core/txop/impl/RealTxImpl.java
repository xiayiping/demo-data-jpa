package org.xyp.project.todoapp.core.txop.impl;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
@Slf4j
public class RealTxImpl implements RealTx {

    @Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = Exception.class)
    public <T> T doReturnInTx(Supplier<T> supplier) {
        return supplier.get();
    }

    @Transactional(value = Transactional.TxType.REQUIRES_NEW, rollbackOn = Exception.class)
    public <T> T doReturnInNewTx(Supplier<T> supplier) {
        return supplier.get();
    }

    @Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = Exception.class)
    public void doRunInTx(Runnable runner) {
        runner.run();
    }

    @Transactional(value = Transactional.TxType.REQUIRES_NEW, rollbackOn = Exception.class)
    public void doRunInNewTx(Runnable runner) {
        runner.run();
    }
}
