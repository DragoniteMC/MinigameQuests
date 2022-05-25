package com.ericlam.mc.mgquests.command;

import com.ericlam.mc.eld.annotations.Commander;
import com.ericlam.mc.eld.annotations.InjectPool;
import com.ericlam.mc.eld.bukkit.CommandNode;
import com.ericlam.mc.eld.configurations.GroupConfig;
import com.ericlam.mc.mgquests.config.GameTable;
import com.ericlam.mc.mgquests.config.QuestConfig;
import com.ericlam.mc.mgquests.config.QuestMessage;
import com.ericlam.mc.mgquests.config.QuestObject;
import org.bukkit.command.CommandSender;

import javax.inject.Inject;

@Commander(
        name = "reload",
        description = "reload configuration"
)
public class QuestsReloadCommand implements CommandNode {

    @Inject
    private QuestConfig config;

    @Inject
    private QuestMessage message;

    @InjectPool
    private GroupConfig<QuestObject> questObjects;

    @InjectPool
    private GroupConfig<GameTable> gameTables;

    @Override
    public void execute(CommandSender commandSender) {
        config.getController().reload();
        message.getController().reload();
        questObjects.fetch();
        gameTables.fetch();
        commandSender.sendMessage(message.getLang().get("reload"));
    }
}
