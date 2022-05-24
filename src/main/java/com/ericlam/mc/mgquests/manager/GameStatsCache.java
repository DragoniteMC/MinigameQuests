package com.ericlam.mc.mgquests.manager;

import java.util.HashMap;
import java.util.Map;

public final class GameStatsCache {
    private final Map<String, Map<String, Double>> gameStatsMap = new HashMap<>();

    public void putGameStats(String gameName, Map<String, Double> stats) {
        gameStatsMap.put(gameName, stats);
    }

    public Map<String, Double> getGameStats(String gameName) {
        return gameStatsMap.get(gameName);
    }

    public double getGameStats(String gameName, String stat){
        var stats = getGameStats(gameName);
        if(stats == null) return -1.0;
        return stats.getOrDefault(stat, -1.0);
    }

}
