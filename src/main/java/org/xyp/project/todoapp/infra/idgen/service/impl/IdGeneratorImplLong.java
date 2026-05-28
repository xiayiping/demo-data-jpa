package org.xyp.project.todoapp.infra.idgen.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import org.xyp.project.todoapp.infra.Provides;
import org.xyp.project.todoapp.infra.idgen.IdGenConfig;
import org.xyp.project.todoapp.infra.idgen.entity.IdTable;
import org.xyp.project.todoapp.infra.idgen.entity.IdTableId;
import org.xyp.project.todoapp.infra.idgen.repository.IdTableRepository;
import org.xyp.project.todoapp.infra.idgen.service.IdGenerator;
import org.xyp.project.todoapp.infra.txop.TransactionalOp;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.LongStream;
import java.util.stream.Stream;

@Slf4j
@AllArgsConstructor
@Service
public class IdGeneratorImplLong implements IdGenerator<Long> {

    final IdTableRepository idTableRepo;
    final TransactionalOp txOp;
    final IdGenConfig config;

    public static final IdTableId KEY_GLOBAL_LOCKER = new IdTableId("global_locker");

    private final Map<String, IdTable> lockers = new ConcurrentHashMap<>();

    @Override
    public Long generate(String keyName) {
        return generate(keyName, 1).getFirst();
    }

    static class Holder<T> {
        T t;
    }

    @Override
    public List<Long> generate(String keyName, long size) {
        val id = new IdTableId(keyName);
        final Holder<List<Long>> longs = new Holder<>();
        lockers.compute(keyName, (String k, IdTable v) -> {
            if (null == v) {
                val idTable = acquireNewIds(id, size);
                longs.t = LongStream.range(idTable.getLastUsedValue() + 1, idTable.getLastUsedValue() + size + 1)
                  .boxed()
                  .toList();
                idTable.setLastUsedValue(idTable.getLastUsedValue() + size);
                return idTable;
            } else if (v.getLastValue() - v.getLastUsedValue() < size) {
                val oldList = LongStream.range(v.getLastUsedValue() + 1, v.getLastValue() + 1).boxed().toList();
                val shorted = size - (v.getLastValue() - v.getLastUsedValue());
                val idTable = acquireNewIds(id, shorted);
                val newList = LongStream.range(idTable.getLastUsedValue() + 1, idTable.getLastUsedValue() + size + 1)
                  .boxed().toList();
                idTable.setLastUsedValue(idTable.getLastUsedValue() + shorted);
                longs.t = Stream.concat(oldList.stream(), newList.stream()).toList();
                return idTable;
            } else {
                longs.t = LongStream.range(v.getLastUsedValue() + 1, v.getLastUsedValue() + size + 1).boxed().toList();
                v.setLastUsedValue(v.getLastUsedValue() + size);
                return v;
            }
        });
        return longs.t;
    }

    private IdTable acquireNewIds(IdTableId id, Long shorted) {
        return withLocked(id, (IdTable fromRepo) -> {
            val divided = shorted / fromRepo.getBatchSize();
            val remained = Provides.from(() -> {
                if (shorted % fromRepo.getBatchSize() == 0L) return 0;
                else return 1;
            });
            val booked = fromRepo.getBatchSize() * (divided + remained);
            fromRepo.setLastUsedValue(fromRepo.getLastValue());
            fromRepo.setLastValue(fromRepo.getLastValue() + booked);
            return fromRepo;
        });
    }

    private IdTable withLocked(
      IdTableId id, Function<IdTable, IdTable> updater
    ) {
        return txOp.returnInNewTx(() -> {
            val foundOpt = idTableRepo.findByIdLocked(id);
            val locked = foundOpt.orElseGet(() -> {
                final Optional<IdTable> _ignored = idTableRepo.findByIdLocked(KEY_GLOBAL_LOCKER);
                val foundAgainOpt = idTableRepo.findByIdLocked(id);
                return foundAgainOpt.orElseGet(() -> {
                    val btSize = config.getDefaultBatchSize();//20L
                    return new IdTable(
                      id,
                      config.getDefaultIdStartsFrom() - 1,
                      btSize,
                      null,
                      null,
                      null,
                      0
                    );
                });
            });
            updater.apply(locked);
            return idTableRepo.save(locked);
        });
    }

}
