package org.xyp.project.todoapp.core.txop.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import org.xyp.project.todoapp.core.txop.TransactionalOp;

import java.util.function.Supplier;

@Slf4j
@Service
@AllArgsConstructor
public class TransactionalOpImpl implements TransactionalOp {

    final RealTx tx;

    @Override
    public <T> T returnInTx(Supplier<T> supplier) {
        log.info("<transaction return>");
        final long start = System.currentTimeMillis();
        try {
            val r = tx.doReturnInTx(supplier);
            log.info("</transaction return> takes [{}]", (System.currentTimeMillis() - start));
            return r;
        } catch (Exception ex) {
            log.error("</transaction return error> takes [{}] {}", System.currentTimeMillis() - start, ex.getMessage(), ex);
            throw ex;
        }
    }

    @Override
    public <T> T returnInNewTx(Supplier<T> supplier) {
        log.info("<transaction return require-new>");
        final long start = System.currentTimeMillis();
        try {
            val r = tx.doReturnInNewTx(supplier);
            log.info("</transaction return require-new> takes [{}]", System.currentTimeMillis() - start);
            return r;
        } catch (Exception ex) {
            log.error("</transaction return require-new error> takes [{}] {}", System.currentTimeMillis() - start, ex.getMessage(), ex);
            throw ex;
        }
    }

    @Override
    public void runInTx(Runnable runner) {
        log.info("<transaction run>");
        final long start = System.currentTimeMillis();
        try {
            tx.doRunInTx(runner);
            log.info("</transaction run> takes [{}]", System.currentTimeMillis() - start);
        } catch (Exception ex) {
            log.error("</transaction run error> takes [{}] {}", System.currentTimeMillis() - start, ex.getMessage(), ex);
            throw ex;
        }
    }

    @Override
    public void runInNewTx(Runnable runner) {
        log.info("<transaction run require-new>");
        final long start = System.currentTimeMillis();
        try {
            tx.doRunInNewTx(runner);
            log.info("</transaction run require-new> takes [{}]", System.currentTimeMillis() - start);
        } catch (Exception ex) {
            log.error("</transaction run require-new error> takes [{}] {}", System.currentTimeMillis() - start, ex.getMessage(), ex);
            throw ex;
        }

    }

}
