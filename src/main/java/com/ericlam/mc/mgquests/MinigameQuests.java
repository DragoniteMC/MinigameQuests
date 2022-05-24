package com.ericlam.mc.mgquests;

import com.ericlam.mc.eld.BukkitManagerProvider;
import com.ericlam.mc.eld.ELDBukkit;
import com.ericlam.mc.eld.ELDBukkitPlugin;
import com.ericlam.mc.eld.ServiceCollection;

@ELDBukkit(
        registry = QuestsRegistry.class,
        lifeCycle = QuestLifeCycle.class
)
public class MinigameQuests extends ELDBukkitPlugin {

    @Override
    protected void manageProvider(BukkitManagerProvider bukkitManagerProvider) {

    }

    @Override
    public void bindServices(ServiceCollection serviceCollection) {

    }

}
