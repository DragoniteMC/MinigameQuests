package com.ericlam.mc.mgquests.dshop;

import com.ericlam.mc.mgquests.manager.QuestsManager;
import org.bukkit.entity.Player;
import org.dragonitemc.dragonshop.api.Condition;

import javax.inject.Inject;

public class QuestAcceptedCondition extends Condition<String> {

    @Inject
    private QuestsManager questsManager;

    public QuestAcceptedCondition() {
        super("quest-accepted");
    }

    @Override
    public boolean isMatched(String s, Player player) {
        return questsManager.isAccepted(player.getUniqueId(), s);
    }
}
