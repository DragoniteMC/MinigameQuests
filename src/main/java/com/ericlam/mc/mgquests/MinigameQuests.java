package com.ericlam.mc.mgquests;

import chu77.eldependenci.sql.SQLInstallation;
import com.ericlam.mc.eld.BukkitManagerProvider;
import com.ericlam.mc.eld.ELDBukkit;
import com.ericlam.mc.eld.ELDBukkitPlugin;
import com.ericlam.mc.eld.ServiceCollection;
import com.ericlam.mc.mgquests.config.GameTable;
import com.ericlam.mc.mgquests.config.QuestConfig;
import com.ericlam.mc.mgquests.config.QuestMessage;
import com.ericlam.mc.mgquests.config.QuestObject;
import com.ericlam.mc.mgquests.db.Quest;
import com.ericlam.mc.mgquests.dshop.AcceptQuestTask;
import com.ericlam.mc.mgquests.dshop.FinishQuestTask;
import com.ericlam.mc.mgquests.dshop.QuestAcceptedCondition;
import com.ericlam.mc.mgquests.dshop.QuestAvailableCondition;
import com.ericlam.mc.mgquests.manager.*;
import com.ericlam.mc.mgquests.papi.QuestPlaceholder;
import com.ericlam.mc.mgquests.repository.QuestRepository;

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

        // configuration
        serviceCollection.addConfiguration(QuestConfig.class);
        serviceCollection.addConfiguration(QuestMessage.class);

        serviceCollection.addGroupConfiguration(QuestObject.class);
        serviceCollection.addGroupConfiguration(GameTable.class);

        // manager
        serviceCollection.addSingleton(DurationConvertManager.class);
        serviceCollection.addSingleton(ProgressManager.class);
        serviceCollection.addSingleton(QuestsManager.class);
        serviceCollection.addSingleton(QuestsStatsManager.class);
        serviceCollection.addSingleton(TableManager.class);

        // hook
        serviceCollection.addSingleton(QuestPlaceholder.class);
        serviceCollection.addSingleton(AcceptQuestTask.class);
        serviceCollection.addSingleton(FinishQuestTask.class);
        serviceCollection.addSingleton(QuestAcceptedCondition.class);
        serviceCollection.addSingleton(QuestAvailableCondition.class);

        // sql register
        SQLInstallation sql = serviceCollection.getInstallation(SQLInstallation.class);
        sql.bindEntities(Quest.class);
        sql.bindJpaRepository(QuestRepository.class);
    }

}
