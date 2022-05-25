package com.ericlam.mc.mgquests.dshop;

import com.ericlam.mc.mgquests.QuestException;
import com.ericlam.mc.mgquests.config.QuestMessage;
import com.ericlam.mc.mgquests.manager.QuestsManager;
import org.bukkit.entity.Player;
import org.dragonitemc.dragonshop.ShopException;
import org.dragonitemc.dragonshop.api.AsyncPriceTask;
import org.dragonitemc.dragonshop.api.PurchaseResult;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

public class AcceptQuestTask extends AsyncPriceTask<String> {

    @Inject
    private QuestsManager questsManager;

    @Inject
    private QuestMessage message;

    public AcceptQuestTask() {
        super("accept-quest");
    }

    @Override
    public CompletableFuture<PurchaseResult> doPurchaseAsync(String s, Player player) {
       try {
           return questsManager.acceptQuest(player.getUniqueId(), s);
       }catch (QuestException e){
           throw new ShopException("設置錯誤", message.getLang().getPure(e.getPath(), e.getArgs()));
       }
    }

    @Override
    public CompletableFuture<Void> doRollBackAsync(String s, Player player) {
        return questsManager.cancelQuest(player.getUniqueId(), s);
    }
}
