package com.ericlam.mc.mgquests;

import com.ericlam.mc.eld.bukkit.CommandNode;
import com.ericlam.mc.eld.bukkit.ComponentsRegistry;
import com.ericlam.mc.eld.registration.CommandRegistry;
import com.ericlam.mc.eld.registration.ListenerRegistry;
import com.ericlam.mc.mgquests.command.QuestsCancelCommand;
import com.ericlam.mc.mgquests.command.QuestsCommand;
import com.ericlam.mc.mgquests.command.QuestsReloadCommand;
import org.bukkit.event.Listener;

import java.util.List;

public class QuestsRegistry implements ComponentsRegistry {
    @Override
    public void registerCommand(CommandRegistry<CommandNode> commandRegistry) {
        commandRegistry.command(QuestsCommand.class, cc -> {
            cc.command(QuestsReloadCommand.class);
            cc.command(QuestsCancelCommand.class);
        });
    }

    @Override
    public void registerListeners(ListenerRegistry<Listener> listenerRegistry) {
        listenerRegistry.listeners(List.of(MinigameQuestListener.class));
    }
}
