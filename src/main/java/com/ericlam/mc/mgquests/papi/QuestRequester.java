package com.ericlam.mc.mgquests.papi;

import com.ericlam.mc.mgquests.QuestException;

import java.util.List;
import java.util.UUID;

@FunctionalInterface
public interface QuestRequester {

    String request(UUID player, List<String> args) throws QuestException;
}
