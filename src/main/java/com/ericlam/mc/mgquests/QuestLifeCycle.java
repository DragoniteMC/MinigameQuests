package com.ericlam.mc.mgquests;

import com.ericlam.mc.eld.bukkit.ELDLifeCycle;
import com.ericlam.mc.mgquests.dshop.AcceptQuestTask;
import com.ericlam.mc.mgquests.dshop.FinishQuestTask;
import com.ericlam.mc.mgquests.dshop.QuestAcceptedCondition;
import com.ericlam.mc.mgquests.dshop.QuestAvailableCondition;
import com.ericlam.mc.mgquests.papi.QuestPlaceholder;
import org.bukkit.plugin.java.JavaPlugin;
import org.dragonitemc.dragonshop.api.ShopTaskService;

import javax.inject.Inject;

public class QuestLifeCycle implements ELDLifeCycle {

    @Inject
    private ShopTaskService taskService;

    @Inject
    private AcceptQuestTask questTask;
    @Inject
    private FinishQuestTask finishQuestTask;
    @Inject
    private QuestAcceptedCondition questAcceptedCondition;
    @Inject
    private QuestAvailableCondition questAvailableCondition;

    @Inject
    private QuestPlaceholder questPlaceholder;


    @Override
    public void onEnable(JavaPlugin javaPlugin) {
        taskService.addPriceTask(questTask);
        taskService.addPriceTask(finishQuestTask);
        taskService.addCondition(questAcceptedCondition);
        taskService.addCondition(questAvailableCondition);
        questPlaceholder.register();
    }

    @Override
    public void onDisable(JavaPlugin javaPlugin) {

    }
}
