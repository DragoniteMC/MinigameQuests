package com.ericlam.mc.mgquests.command;

import com.ericlam.mc.eld.annotations.Commander;

import java.util.concurrent.CompletableFuture;

@Commander(
        name = "cancel",
        description = "取消任務"
)
public class QuestsCancelCommand extends QuestsAbstractCommand {

    @Override
    public CompletableFuture<String> doQuestAction() {
        return questsManager.cancelQuest(player.getUniqueId(), questId).thenApply(SUCCESS_RESULT);
    }

}
