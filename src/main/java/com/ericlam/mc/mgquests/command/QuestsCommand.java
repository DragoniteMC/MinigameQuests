package com.ericlam.mc.mgquests.command;

import com.ericlam.mc.eld.annotations.Commander;
import com.ericlam.mc.eld.bukkit.CommandNode;
import org.bukkit.command.CommandSender;

@Commander(
        name = "quests",
        description = "quests 主指令",
        permission = "mgquests.admin"
)
public class QuestsCommand implements CommandNode {
    @Override
    public void execute(CommandSender commandSender) {

    }
}
