package com.ericlam.mc.mgquests.command;

import com.ericlam.mc.eld.annotations.CommandArg;
import com.ericlam.mc.eld.bukkit.CommandNode;
import com.ericlam.mc.mgquests.QuestException;
import com.ericlam.mc.mgquests.config.QuestMessage;
import com.ericlam.mc.mgquests.manager.QuestsManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public abstract class QuestsAbstractCommand implements CommandNode {

    protected static final Function<Object, String> SUCCESS_RESULT = v -> "";

    @Inject
    protected QuestsManager questsManager;

    @Inject
    protected QuestMessage message;

    @CommandArg(order = 1)
    protected String questId;

    @CommandArg(order = 2, optional = true)
    protected OfflinePlayer player;

    @Override
    public void execute(CommandSender commandSender) {
        if (player == null) {

            if (!(commandSender instanceof Player p)) {
                commandSender.sendMessage("§c你必須是玩家才能使用此指令");
                return;
            } else {
                this.player = p;
            }

        }

        try {

            doQuestAction().whenComplete((v, ex) -> {
                if (ex != null) {
                    ex.printStackTrace();
                    commandSender.sendMessage(message.getLang().get("operation-failed", ex.getMessage()));
                } else if (!v.isBlank()) {
                    commandSender.sendMessage(message.getLang().get("operation-failed", v));
                } else {
                    commandSender.sendMessage(message.getLang().get("operation-success"));
                }
            });

        } catch (QuestException e) {
            commandSender.sendMessage(message.getLang().get(e.getPath(), e.getArgs()));
        }
    }

    public abstract CompletableFuture<String> doQuestAction() throws QuestException;

}
