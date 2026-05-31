package org.xyp.todoapp.core.idgen.impl

import org.springframework.stereotype.Service
import org.xyp.todoapp.core.idgen.IdGenProperties
import org.xyp.todoapp.core.idgen.IdGenerator
import org.xyp.todoapp.core.idgen.entity.IdTable
import org.xyp.todoapp.core.idgen.entity.IdTableId
import org.xyp.todoapp.core.idgen.repo.IdTableRepo
import org.xyp.todoapp.core.txop.TransactionalOp
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.function.Function
import java.util.stream.LongStream

@Service
class IdGeneratorIplLong(
    val idTableRepo: IdTableRepo,
    val txOp: TransactionalOp,
    val config: IdGenProperties,
) : IdGenerator<Long> {

    companion object {
        val KEY_GLOBAL_LOCKER: IdTableId = IdTableId("global_locker")
    }

    private val lockers: MutableMap<String, IdTable> = ConcurrentHashMap<String, IdTable>()

    override fun generate(keyName: String): Long {
        return generate(keyName, 1)[0]
    }


    override fun generate(keyName: String, size: Long): List<Long> {
        val id = IdTableId(keyName)
        val longs = Holder<List<Long>>()
        lockers.compute(keyName) { _: String, v: IdTable? ->
            if (null == v) {
                val idTable = acquireNewIds(id, size)
                longs.t = LongStream.range(idTable.lastUsedValue + 1, idTable.lastUsedValue + size + 1)
                    .boxed()
                    .toList()
                idTable.lastUsedValue = (idTable.lastUsedValue + size)
                idTable
            } else if (v.lastValue - v.lastUsedValue < size) {
                val oldList = LongStream.range(v.lastUsedValue + 1, v.lastValue + 1).boxed().toList()
                val shorted = size - (v.lastValue - v.lastUsedValue)
                val idTable = acquireNewIds(id, shorted)
                val newList =
                    LongStream.range(idTable.lastUsedValue + 1, idTable.lastUsedValue + size + 1)
                        .boxed().toList()
                idTable.lastUsedValue = (idTable.lastUsedValue + shorted)
                longs.t = oldList + newList
                idTable
            } else {
                longs.t = LongStream.range(v.lastUsedValue + 1, v.lastUsedValue + size + 1).boxed().toList()
                v.lastUsedValue = (v.lastUsedValue + size)
                v
            }
        }
        return longs.t!!
    }

    private fun acquireNewIds(id: IdTableId, shorted: Long): IdTable {
        return withLocked(id) { fromRepo: IdTable ->
            val divided = shorted / fromRepo.batchSize
            val remained =
                if (shorted % fromRepo.batchSize == 0L) 0
                else 1

            val booked = fromRepo.batchSize * (divided + remained)
            fromRepo.lastUsedValue = (fromRepo.lastValue)
            fromRepo.lastValue = (fromRepo.lastValue + booked)
            fromRepo
        }
    }

    private fun withLocked(
        id: IdTableId, updater: Function<IdTable, IdTable>
    ): IdTable {
        return txOp.returnInNewTx({
            val foundOpt = idTableRepo.findByIdLocked(id)
            val locked = foundOpt ?: run {
                idTableRepo.findByIdLocked(KEY_GLOBAL_LOCKER)
                val foundAgainOpt = idTableRepo.findByIdLocked(id)
                foundAgainOpt ?: run {
                    val btSize = config.defaultBatchSize //20L
                    IdTable(
                        id,
                        config.defaultIdStartsFrom - 1,
                        btSize,
                        null,
                        null,
                        null,
                        0
                    )
                }
            }
            updater.apply(locked)
            idTableRepo.save(locked)
        })!!
    }
}

data class Holder<T>(
    var t: T? = null
)