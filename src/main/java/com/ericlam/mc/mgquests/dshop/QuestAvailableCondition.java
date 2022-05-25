package com.ericlam.mc.mgquests.dshop;

import com.ericlam.mc.mgquests.QuestException;
import com.ericlam.mc.mgquests.config.QuestMessage;
import com.ericlam.mc.mgquests.manager.QuestsManager;
import org.bukkit.entity.Player;
import org.dragonitemc.dragonshop.ShopException;
import org.dragonitemc.dragonshop.api.Condition;

import javax.inject.Inject;

public class QuestAvailableCondition extends Condition<String> {

    @Inject
    private QuestsManager questsManager;

    @Inject
    private QuestMessage message;

    public QuestAvailableCondition() {
        super("quest-available");
    }

    @Override
    public boolean isMatched(String s, Player player) {
        try {
            return questsManager.isAvailable(player.getUniqueId(), s);
        }catch (QuestException e){
            throw new ShopException("設置錯誤", message.getLang().getPure(e.getPath(), e.getArgs()));
        }
    }
}
