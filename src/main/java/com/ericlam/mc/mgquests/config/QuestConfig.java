package com.ericlam.mc.mgquests.config;

import com.ericlam.mc.eld.annotations.Resource;
import com.ericlam.mc.eld.components.Configuration;

@Resource(locate = "config.yml")
public class QuestConfig extends Configuration {

    public int progress_bar_count;

}
