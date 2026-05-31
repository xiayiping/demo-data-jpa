package org.xyp.todoapp.core.txop.impl

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class RealTx {

    @Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = [Exception::class])
    fun <T> doReturnInTx(supplier: () -> T?): T? {
        return supplier()
    }

    @Transactional(value = Transactional.TxType.REQUIRES_NEW, rollbackOn = [Exception::class])
    fun <T> doReturnInNewTx(supplier: () -> T?): T? {
        return supplier()
    }

    @Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = [Exception::class])
    fun doRunInTx(runner: () -> Unit) {
        runner()
    }

    @Transactional(value = Transactional.TxType.REQUIRES_NEW, rollbackOn = [Exception::class])
    fun doRunInNewTx(runner: () -> Unit) {
        runner()
    }
}