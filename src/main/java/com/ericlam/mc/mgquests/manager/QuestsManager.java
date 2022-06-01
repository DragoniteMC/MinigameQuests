package com.ericlam.mc.mgquests.manager;

import com.ericlam.mc.eld.annotations.InjectPool;
import com.ericlam.mc.eld.configurations.GroupConfig;
import com.ericlam.mc.mgquests.QuestException;
import com.ericlam.mc.mgquests.config.QuestMessage;
import com.ericlam.mc.mgquests.config.QuestObject;
import com.ericlam.mc.mgquests.db.Quest;
import org.dragonitemc.dragonshop.api.PurchaseResult;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class QuestsManager {


    @Inject
    private QuestsStatsManager questsStatsManager;

    @Inject
    private ProgressManager progressManager;

    @Inject
    private DurationConvertManager durationConvertManager;

    @Inject
    private QuestMessage message;


    @InjectPool
    private GroupConfig<QuestObject> questObjects;

    public CompletableFuture<PurchaseResult> acceptQuest(UUID player, String questId) throws QuestException {
        QuestObject questObject = questObjects.findById(questId).orElseThrow(() -> new QuestException("quest-not-exist", questId));
        var stats = questsStatsManager.getPlayerStats(player);
        var quest = stats.getQuest(questId);
        if (quest == null){
            quest = new Quest();
            quest.user = player;
            quest.quest = questId;
        } else if (questObject.coolDown != null && quest.lastFinished != 0){
           var coolDownEnd = durationConvertManager.getCoolDownEndTime(quest, questObject.coolDown);
            if (coolDownEnd.isBefore(LocalDateTime.now())){
                var err = message.getLang().get("quest-unavailable", questId);
                return CompletableFuture.completedFuture(PurchaseResult.failed(err));
            }
        }
        final var q = quest;
        return questsStatsManager.hasReachedMaximum(player).thenCompose(reached -> {
            if (reached){
                return CompletableFuture.completedFuture(PurchaseResult.failed(message.getLang().get("max-quest-reached", questId)));
            } else {
                q.lastStarted = Timestamp.from(Instant.now()).getTime();
                return questsStatsManager.updateQuests(player, q).thenApply(v -> PurchaseResult.success());
            }
        });
    }

    public CompletableFuture<Void> cancelQuest(UUID player, String questId) {
        return questsStatsManager.removeQuests(player, questId);
    }

    public boolean isAvailable(UUID player, String questId) throws QuestException {
        QuestObject questObject = questObjects.findById(questId).orElseThrow(() -> new QuestException("quest-not-exist", questId));
        var stats = questsStatsManager.getPlayerStats(player);
        var quest = stats.getQuest(questId);
        if (quest == null){
            return true;
        } else if (questObject.coolDown != null && quest.lastFinished != 0){
            var coolDownEnd = durationConvertManager.getCoolDownEndTime(quest, questObject.coolDown);
            return coolDownEnd.isBefore(LocalDateTime.now());
        }
        return true;
    }

    public boolean isAccepted(UUID player, String questId) {
        var quest = questsStatsManager.getPlayerStats(player).getQuest(questId);
        if (quest == null) return false;
        return quest.lastStarted > quest.lastFinished;
    }

    public PurchaseResult tryFinishQuest(UUID player, String questId) throws QuestException{
        var questObject = questObjects.findById(questId).orElseThrow(() -> new QuestException("quest-not-exist", questId));
        var gameStats = progressManager.getProgressCache(player);
        var questStats = questsStatsManager.getPlayerStats(player);
        var quest = questStats.getQuest(questId);
        if (quest == null){
            return PurchaseResult.failed(message.getLang().get("quest-unavailable", questId));
        }
        // check deadline is passed
        var deadline = durationConvertManager.getDeadline(quest, questObject.timeLimit);
        if (deadline.isBefore(LocalDateTime.now())) return PurchaseResult.failed(message.getLang().get("quest-unavailable", questId));
        // check all stats is passed
        for (String targetStat : questObject.targets.keySet()) {
            var passNum = questObject.targets.get(targetStat);
            var actualNum = gameStats.getProgress(questObject.getId(), targetStat);
            if (actualNum < passNum) return PurchaseResult.failed(message.getLang().get("quest-not-finished", questId));
        }
        return PurchaseResult.success();
    }

    public double getTargetCount(String questId) throws QuestException {
        var questObject = questObjects.findById(questId).orElseThrow(() -> new QuestException("quest-not-exist", questId));
        if (questObject.targets == null) return 0;
        return questObject.targets.values().stream().mapToDouble(v -> v).sum();
    }

    public double getFinishedCount(UUID player, String questId) throws QuestException {
        if (!isAccepted(player, questId)) return 0;
        var stats = getProgressStats(player, questId);
        if (stats == null) return 0;
        return stats.values().stream().mapToDouble(v -> v).sum();
    }

    private Map<String, Double> getProgressStats(UUID player, String questId) throws QuestException {
        var gameStats = progressManager.getProgressCache(player);
        var questObject = questObjects.findById(questId).orElseThrow(() -> new QuestException("quest-not-exist", questId));
        return gameStats.getProgress(questObject.getId());
    }

    public double getFinishedCount(UUID player, String questId, String stat) throws QuestException {
        if (!isAccepted(player, questId)) return 0;
        var stats = getProgressStats(player, questId);
        if (stats == null) return 0;
        return stats.getOrDefault(stat, 0.0);
    }


    public CompletableFuture<PurchaseResult> finishQuest(UUID player, String questId) throws QuestException {
        var result = tryFinishQuest(player, questId);
        if (!result.isSuccess()) {
            return CompletableFuture.completedFuture(result);
        }
        var gameStats = progressManager.getProgressCache(player);
        var questStats = questsStatsManager.getPlayerStats(player);
        var quest = questStats.getQuest(questId);
        if (quest == null) throw new QuestException("quest-unavailable", questId);
        quest.lastFinished = Timestamp.from(Instant.now()).getTime();
        gameStats.clearProgress(questId);
        return questsStatsManager.updateQuests(player, quest).thenApply(v -> result);
    }

}
