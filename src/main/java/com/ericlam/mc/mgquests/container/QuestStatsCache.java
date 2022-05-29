package com.ericlam.mc.mgquests.container;

import com.ericlam.mc.mgquests.db.Quest;
import com.google.common.collect.ImmutableMap;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class QuestStatsCache {

    private final Map<String, Quest> questMap = new ConcurrentHashMap<>();


    public Map<String, Quest> getQuestMap() {
        return ImmutableMap.copyOf(questMap);
    }

    public void updateQuest(String questId, Quest quest) {
        questMap.put(questId, quest);
    }

    @Nullable
    public Quest getQuest(String questId) {
        return questMap.get(questId);
    }

    public void removeQuest(String questId) {
        questMap.remove(questId);
    }


}
