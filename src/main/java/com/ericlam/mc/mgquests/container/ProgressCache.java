package com.ericlam.mc.mgquests.container;

import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ProgressCache {
    private final Map<String, Map<String, Double>> progressMap = new ConcurrentHashMap<>();

    public void updateProgress(String questId, Map<String, Double> stats) {
        progressMap.put(questId, stats);
    }

    @Nullable
    public Map<String, Double> getProgress(String questId) {
        return progressMap.get(questId);
    }

    public double getProgress(String questId, String stat) {
        var stats = getProgress(questId);
        if (stats == null) return -1.0;
        return stats.getOrDefault(stat, -1.0);
    }

    public void clearProgress(String questId){
        progressMap.remove(questId);
    }

}
