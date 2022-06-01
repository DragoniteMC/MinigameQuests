package com.ericlam.mc.mgquests.papi;

import com.ericlam.mc.mgquests.MinigameQuests;
import com.ericlam.mc.mgquests.QuestException;
import com.ericlam.mc.mgquests.config.QuestConfig;
import com.ericlam.mc.mgquests.config.QuestMessage;
import com.ericlam.mc.mgquests.manager.QuestsManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;
import java.util.*;

public class QuestPlaceholder extends PlaceholderExpansion {

    @Inject
    private MinigameQuests plugin;

    @Inject
    private QuestMessage message;

    @Inject
    private QuestConfig config;

    private final Map<String, QuestRequester> questPlaceholders = new HashMap<>();

    @Inject
    private QuestsManager questsManager;

    public QuestPlaceholder() {
        this.questPlaceholders.put("available", (uuid, args) -> {
            var quest = args.get(0);
            return String.valueOf(questsManager.isAvailable(uuid, quest));
        });

        this.questPlaceholders.put("accepted", (uuid, args) -> {
            var quest = args.get(0);
            return String.valueOf(questsManager.isAccepted(uuid, quest));
        });

        this.questPlaceholders.put("completed", (uuid, args) -> {
            var quest = args.get(0);
            return String.valueOf(questsManager.tryFinishQuest(uuid, quest).isSuccess());
        });

        this.questPlaceholders.put("target", (uuid, args) -> {
            var quest = args.get(0);
            var useInt = args.size() > 1 && args.get(1).equalsIgnoreCase("int");
            var target = questsManager.getTargetCount(quest);
            return useInt ? String.format("%d", Math.round(target)) : String.format("%.2f", target);
        });

        this.questPlaceholders.put("finished", (uuid, args) -> {
            var quest = args.get(0);
            var useInt = args.size() > 1 && args.get(1).equalsIgnoreCase("int");
            var finished = questsManager.getFinishedCount(uuid, quest);
            return useInt ? String.format("%d", Math.round(finished)) : String.format("%.2f", finished);
        });

        this.questPlaceholders.put("progress", (uuid, args) -> {
            var quest = args.get(0);
            var useInt = args.size() > 1 && args.get(1).equalsIgnoreCase("int");
            var max = questsManager.getTargetCount(quest);
            var min = questsManager.getFinishedCount(uuid, quest);
            return useInt ? String.format("%d/%d", Math.round(min), Math.round(max)) : String.format("%.2f/%.2f", min, max);
        });

        this.questPlaceholders.put("finished-stats", (uuid, args) -> {
            var quest = args.get(0);
            var stat = args.get(1);
            var useInt = args.size() > 2 && args.get(2).equalsIgnoreCase("int");
            var finished = questsManager.getFinishedCount(uuid, quest, stat);
            return useInt ? String.format("%d", Math.round(finished)) : String.format("%.2f", finished);
        });

        this.questPlaceholders.put("progressbar", (uuid, args) -> {
            var quest = args.get(0);
            var max = questsManager.getTargetCount(quest);
            var current = questsManager.getFinishedCount(uuid, quest);

            var bar = message.getLang().getPure("progress-bar");
            var barFill = message.getLang().getPure("progress-bar-finished");
            var width = config.progress_bar_count;

            int finished = (int) Math.ceil(current / max * width);
            int unfinished = width - finished;

            return String.valueOf(barFill).repeat(Math.max(0, finished)) +
                    String.valueOf(bar).repeat(Math.max(0, unfinished));
        });

    }


    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        var args = params.split("_");
        if (args.length < 2) return null;
        var argList = new ArrayList<>(Arrays.asList(args));
        var questName = argList.remove(0);
        var papiFunc = this.questPlaceholders.get(questName);
        if (papiFunc == null) return null;
        try {
            return papiFunc.request(player.getUniqueId(), argList);
        } catch (IndexOutOfBoundsException e) {
            return e.getMessage();
        } catch (QuestException e) {
            return message.getLang().getPure(e.getPath(), e.getArgs());
        }
    }

    @Override
    public @NotNull String getIdentifier() {
        return plugin.getName().toLowerCase(Locale.ROOT);
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }
}
