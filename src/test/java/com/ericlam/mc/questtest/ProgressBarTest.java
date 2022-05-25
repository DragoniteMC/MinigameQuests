package com.ericlam.mc.questtest;

public class ProgressBarTest {

    private static final String BAR = "A";
    private static final String FINISHED = "B";

    public static void main(String[] args) {
        int max = 16;
        int current = 7;
        int width = 16;
        System.out.println(getProgressBar(max, current, width));
    }

    public static String getProgressBar(int max, int current, int width) {
        StringBuilder sb = new StringBuilder();
        int finished = (int) Math.ceil((double) current / max * width);
        int unfinished = width - finished;
        sb.append(FINISHED.repeat(Math.max(0, finished)));
        sb.append(BAR.repeat(Math.max(0, unfinished)));
        return sb.toString();
    }
}
