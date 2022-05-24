package com.ericlam.mc.mgquests.manager;

import com.ericlam.mc.eld.annotations.InjectPool;
import com.ericlam.mc.eld.configurations.GroupConfig;
import com.ericlam.mc.mgquests.QuestException;
import com.ericlam.mc.mgquests.config.GameTable;
import com.ericlam.mc.mgquests.dshop.QuestObject;

import javax.inject.Inject;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class QuestsManager {

    @InjectPool
    private GroupConfig<GameTable> gameTables;

    @Inject
    private TableManager tableManager;

    private final Map<String, Function<Long, Duration>> durationConverterMap = new HashMap<>();

    public QuestsManager() {
        durationConverterMap.put("minutes", Duration::ofMinutes);
        durationConverterMap.put("hours", Duration::ofHours);
        durationConverterMap.put("days", Duration::ofDays);
        durationConverterMap.put("months", num -> Duration.ofDays(num * 30));
        durationConverterMap.put("weeks", num -> Duration.ofDays(num * 7));
    }


    public CompletableFuture<Map<String, Double>> getResultStats(UUID player, QuestObject object) throws QuestException {
        var tableOpt = gameTables.findById(object.type);
        if (tableOpt.isEmpty()) throw new QuestException("table-not-exist", object.type);
        var table = tableOpt.get();
        var timeLimit = object.timeLimit;
        if (timeLimit == null) throw new QuestException("time-limit-not-set");
        if (object.stats == null) throw new QuestException("stats-not-set");
        var durFunc = durationConverterMap.get(timeLimit.type);
        if (durFunc == null) throw new QuestException("time-limit-not-exist", timeLimit.type);
        for (String stat : object.stats) {
            if (!table.statsColumns.contains(stat)) {
                throw new QuestException("stat-not-exist", stat);
            }
        }
        return CompletableFuture.supplyAsync(() -> tableManager.getTableStats(player, table, durFunc.apply(timeLimit.time), object.stats));
    }

}
