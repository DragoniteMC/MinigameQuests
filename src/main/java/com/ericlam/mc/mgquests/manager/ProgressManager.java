package com.ericlam.mc.mgquests.manager;

import com.ericlam.mc.eld.annotations.InjectPool;
import com.ericlam.mc.eld.configurations.GroupConfig;
import com.ericlam.mc.eld.misc.DebugLogger;
import com.ericlam.mc.eld.services.LoggingService;
import com.ericlam.mc.mgquests.QuestException;
import com.ericlam.mc.mgquests.config.GameTable;
import com.ericlam.mc.mgquests.config.QuestObject;
import com.ericlam.mc.mgquests.container.ProgressCache;
import com.ericlam.mc.mgquests.container.QuestStatsCache;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class ProgressManager {

    @InjectPool
    private GroupConfig<GameTable> gameTables;

    @InjectPool
    private GroupConfig<QuestObject> questObjects;

    @Inject
    private TableManager tableManager;

    @Inject
    private DurationConvertManager durationConvertManager;


    private final DebugLogger logger;

    @Inject
    public ProgressManager(LoggingService loggingService){
        this.logger = loggingService.getLogger(QuestsManager.class);
    }

    private final Map<UUID, ProgressCache> progressCacheMap = new ConcurrentHashMap<>();


    public ProgressCache getProgressCache(UUID uuid) {
        return progressCacheMap.computeIfAbsent(uuid, id -> new ProgressCache());
    }

    public CompletableFuture<Void> loadAllProgresses(UUID player, QuestStatsCache statsCache) throws QuestException{
        var futures = new ArrayList<CompletableFuture<Void>>();
        var questStatsMap = statsCache.getQuestMap();
        logger.debugF("preparing to load %d ongoing quests for player %s", questStatsMap.size(), player);
        for (var quest : questStatsMap.values()) {
            logger.debugF("loading quest %s for player %s", quest.quest, player);
            var questObjectOpt = questObjects.findById(quest.quest);
            if (questObjectOpt.isEmpty()) {
                logger.debugF("cannot find quest %s from QuestObjects, ignored", quest.quest);
                continue;
            }
            var questObject = questObjectOpt.get();
            if (questObject.coolDown != null && quest.lastFinished != 0) {
                var coolDownEnd = durationConvertManager.getCoolDownEndTime(quest, questObject.coolDown);
                if (coolDownEnd.isBefore(LocalDateTime.now())) {
                    logger.debugF("quest %s is cooled down, ignored (deadline: %s)", quest.quest, coolDownEnd);
                    continue;
                }
            }
            futures.add(getResultStats(player, questObject, quest.lastStarted).thenAccept(map -> {
                var cache = progressCacheMap.computeIfAbsent(player, uuid -> new ProgressCache());
                cache.updateProgress(questObject.getId(), map);
            }));
        }
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    public CompletableFuture<Map<String, Double>> getResultStats(UUID player, QuestObject object, long lastStarted) throws QuestException {
        var tableOpt = gameTables.findById(object.type);
        if (tableOpt.isEmpty()) throw new QuestException("table-not-exist", object.type);
        var table = tableOpt.get();
        var timeLimit = object.timeLimit;
        if (timeLimit == null) throw new QuestException("time-limit-not-set");
        if (object.targets == null) throw new QuestException("stats-not-set");
        var durFunc = durationConvertManager.getConverter(timeLimit.type);
        if (durFunc == null) throw new QuestException("time-limit-not-exist", timeLimit.type);
        for (String stat : object.targets.keySet()) {
            if (!table.statsColumns.contains(stat)) {
                throw new QuestException("stat-not-exist", stat);
            }
        }
        return CompletableFuture.supplyAsync(() -> tableManager.getTableStats(player, table, durFunc.apply(timeLimit.time), object.targets.keySet(), lastStarted));
    }

}
