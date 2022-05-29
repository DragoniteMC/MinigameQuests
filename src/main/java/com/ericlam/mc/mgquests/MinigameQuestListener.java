package com.ericlam.mc.mgquests;

import com.ericlam.mc.mgquests.config.QuestMessage;
import com.ericlam.mc.mgquests.manager.ProgressManager;
import com.ericlam.mc.mgquests.manager.QuestsStatsManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

public class MinigameQuestListener implements Listener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MinigameQuestListener.class);
    @Inject
    private QuestsStatsManager questsStatsManager;

    @Inject
    private ProgressManager progressManager;

    @Inject
    private QuestMessage message;


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        var player = e.getPlayer();
        questsStatsManager.loadPlayerStats(player.getUniqueId())
                .thenCompose(cache -> {
                    try {
                        return progressManager.loadAllProgresses(player.getUniqueId(), cache);
                    } catch (QuestException qe) {
                        var err = message.getLang().get(qe.getPath(), qe.getArgs());
                        LOGGER.warn(err);
                        player.sendMessage(err);
                        return CompletableFuture.completedFuture(null);
                    }
                }).whenComplete((v, ex) -> {
                    if (ex != null) {
                        LOGGER.error("Error when loading player stats", ex);
                        player.sendMessage("&c任務資料加載失敗: " + ex.getMessage());
                    } else {
                        LOGGER.info("Player {} quests stats updated", player.getName());
                    }
                });

    }
}
