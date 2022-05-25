package com.ericlam.mc.mgquests.container;

import com.ericlam.mc.mgquests.db.Quest;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class QuestStatsCache {

    private final Map<String, Quest> questMap = new ConcurrentHashMap<>();

    public void updateQuest(String questId, Quest quest) {
        questMap.put(questId, quest);
    }

    public Quest getQuest(String questId) {
        return questMap.get(questId);
    }

    public void removeQuest(String questId) {
        questMap.remove(questId);
    }


}
