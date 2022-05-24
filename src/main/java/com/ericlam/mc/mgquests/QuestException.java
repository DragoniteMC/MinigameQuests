package com.ericlam.mc.mgquests;

public class QuestException extends Exception {

    private final String path;
    private final Object[] args;

    public QuestException(String path, Object... args) {
        this.path = path;
        this.args = args;
    }

    public String getPath() {
        return path;
    }

    public Object[] getArgs() {
        return args;
    }
}
