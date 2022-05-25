package com.ericlam.mc.mgquests.config;

import com.ericlam.mc.eld.annotations.Prefix;
import com.ericlam.mc.eld.annotations.Resource;
import com.ericlam.mc.eld.components.LangConfiguration;

@Resource(locate = "lang.yml")
@Prefix(path = "prefix")
public class QuestMessage extends LangConfiguration {
}
