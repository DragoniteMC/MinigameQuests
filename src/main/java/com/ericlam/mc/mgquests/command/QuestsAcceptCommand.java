package com.ericlam.mc.mgquests.command;

import com.ericlam.mc.eld.annotations.Commander;
import com.ericlam.mc.mgquests.QuestException;

import java.util.concurrent.CompletableFuture;

@Commander(
        name = "accept",
        description = "Accept a quest"
)
public class QuestsAcceptCommand extends QuestsAbstractCommand {

    @Override
    public CompletableFuture<String> doQuestAction() throws QuestException {
        return questsManager.acceptQuest(player.getUniqueId(), questId).thenApply(f -> f.isSuccess() ? "" : f.getMessage());
    }

}
