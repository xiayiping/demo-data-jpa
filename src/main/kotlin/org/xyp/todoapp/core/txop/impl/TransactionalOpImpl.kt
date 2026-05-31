package org.xyp.todoapp.core.txop.impl

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.xyp.todoapp.core.txop.TransactionalOp

@Service
class TransactionalOpImpl(val tx: RealTx) : TransactionalOp {
    companion object {
        val log = LoggerFactory.getLogger(TransactionalOpImpl::class.java)
    }

    override fun <T> returnInTx(supplier: () -> T?): T? {
        log.info("<transaction return>")
        val start = System.currentTimeMillis()
        try {
            val r = tx.doReturnInTx(supplier)
            log.info("</transaction return> takes [{}]", (System.currentTimeMillis() - start))
            return r
        } catch (ex: Exception) {
            log.error("</transaction return error> takes [{}] {}", System.currentTimeMillis() - start, ex.message, ex)
            throw ex
        }
    }

    override fun <T> returnInNewTx(supplier: () -> T?): T? {
        log.info("<transaction return require-new>")
        val start = System.currentTimeMillis()
        try {
            val r = tx.doReturnInNewTx(supplier)
            log.info("</transaction return require-new> takes [{}]", System.currentTimeMillis() - start)
            return r
        } catch (ex: Exception) {
            log.error("</transaction return require-new error> takes [{}] {}",
                System.currentTimeMillis() - start,
                ex.message,
                ex)
            throw ex
        }
    }

    override fun runInTx(runner: () -> Unit) {
        log.info("<transaction run>")
        val start = System.currentTimeMillis()
        try {
            tx.doRunInTx(runner)
            log.info("</transaction run> takes [{}]", System.currentTimeMillis() - start)
        } catch (ex: Exception) {
            log.error("</transaction run error> takes [{}] {}", System.currentTimeMillis() - start, ex.message, ex)
            throw ex
        }
    }

    override fun runInNewTx(runner: () -> Unit) {
        log.info("<transaction run require-new>")
        val start = System.currentTimeMillis()
        try {
            tx.doRunInNewTx(runner)
            log.info("</transaction run require-new> takes [{}]", System.currentTimeMillis() - start)
        } catch (ex: Exception) {
            log.error("</transaction run require-new error> takes [{}] {}",
                System.currentTimeMillis() - start,
                ex.message,
                ex)
            throw ex
        }
    }
}