package com.ericlam.mc.mgquests.manager;

import com.ericlam.mc.mgquests.container.QuestStatsCache;
import com.ericlam.mc.mgquests.db.Quest;
import com.ericlam.mc.mgquests.repository.QuestRepository;

import javax.inject.Inject;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class QuestsStatsManager {

    private final Map<UUID, QuestStatsCache> questMap = new ConcurrentHashMap<>();

    @Inject
    private QuestRepository questRepository;


    public CompletableFuture<QuestStatsCache> loadPlayerStats(UUID player) {
        return CompletableFuture.supplyAsync(() -> {
            var list = questRepository.findByUser(player);
            var cache = questMap.computeIfAbsent(player, k -> new QuestStatsCache());
            for (Quest quest : list) {
                cache.updateQuest(quest.quest, quest);
            }
            questMap.put(player, cache);
            return cache;
        });
    }


    public QuestStatsCache getPlayerStats(UUID player) {
        return questMap.computeIfAbsent(player, id -> new QuestStatsCache());
    }


    public CompletableFuture<Void> updateQuests(UUID player, Quest quest){
        return CompletableFuture.runAsync(() -> {
            questRepository.save(quest);
            var cache = getPlayerStats(player);
            cache.updateQuest(quest.quest, quest);
        });
    }

    public CompletableFuture<Void> removeQuests(UUID player, String quest){
        return CompletableFuture.runAsync(() -> {
            questRepository.deleteById(new Quest.Quester(quest, player));
            var cache = getPlayerStats(player);
            cache.removeQuest(quest);
        });
    }



}
