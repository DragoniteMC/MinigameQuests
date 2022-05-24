package com.ericlam.mc.mgquests.dshop;

import java.util.List;

public class QuestObject {

    public String type;
    public List<String> stats;
    public double target;
    public TimeLimit timeLimit;

    public static class TimeLimit {
        public long time;
        public String type;
    }

}
