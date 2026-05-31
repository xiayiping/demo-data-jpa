package org.xyp.todoapp.core.txop

interface TransactionalOp {
    fun <T> returnInTx(supplier: () -> T?): T?

    fun <T> returnInNewTx(supplier: () -> T?): T?

    fun runInTx(runner: () -> Unit)

    fun runInNewTx(runner: () -> Unit)
}