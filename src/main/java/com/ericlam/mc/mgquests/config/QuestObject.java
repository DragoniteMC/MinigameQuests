package com.ericlam.mc.mgquests.config;

import com.ericlam.mc.eld.annotations.GroupResource;
import com.ericlam.mc.eld.components.GroupConfiguration;

import java.util.Map;

@GroupResource(
        folder = "quests",
        preloads = {"win-5-mcinf-hourly", "kill-20-mcinf-daily"}
)
public class QuestObject extends GroupConfiguration {

    public String type;
    public Map<String, Double> targets;
    public TimeDuration timeLimit;

    public TimeDuration coolDown;

    public static class TimeDuration {
        public long time;
        public String type;
    }

}
