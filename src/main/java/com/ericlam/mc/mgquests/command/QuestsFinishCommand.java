package com.ericlam.mc.mgquests.command;

import com.ericlam.mc.eld.annotations.Commander;
import com.ericlam.mc.mgquests.QuestException;

import java.util.concurrent.CompletableFuture;

@Commander(
        name = "finish",
        description = "Finish a quest"
)
public class QuestsFinishCommand extends QuestsAbstractCommand{

    @Override
    public CompletableFuture<String> doQuestAction() throws QuestException {
        return questsManager.finishQuest(player.getUniqueId(), questId).thenApply(f -> f.isSuccess() ? "" : f.getMessage());
    }

}
