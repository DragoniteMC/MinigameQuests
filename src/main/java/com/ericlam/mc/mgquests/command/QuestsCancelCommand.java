package com.ericlam.mc.mgquests.command;

import com.ericlam.mc.eld.annotations.CommandArg;
import com.ericlam.mc.eld.annotations.Commander;
import com.ericlam.mc.eld.bukkit.CommandNode;
import com.ericlam.mc.mgquests.config.QuestMessage;
import com.ericlam.mc.mgquests.manager.QuestsManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

@Commander(
        name = "cancel",
        description = "取消任務"
)
public class QuestsCancelCommand implements CommandNode {

    @Inject
    private QuestsManager questsManager;

    @Inject
    private QuestMessage message;

    @CommandArg(order = 1)
    private String questId;

    @CommandArg(order = 2, optional = true)
    private OfflinePlayer player;

    @Override
    public void execute(CommandSender commandSender) {
        if (player == null){

            if (!(commandSender instanceof Player p)){
                commandSender.sendMessage("§c你必須是玩家才能使用此指令");
                return;
            } else {
                this.player = p;
            }

        }

        questsManager.cancelQuest(player.getUniqueId(), questId).whenComplete((v, ex) -> {
            if (ex != null){
                ex.printStackTrace();
                commandSender.sendMessage(message.getLang().get("operation-failed", ex.getMessage()));
            }else{
                commandSender.sendMessage(message.getLang().get("operation-success"));
            }
        });
    }

}
