package com.ericlam.mc.mgquests.config;

import com.ericlam.mc.eld.annotations.GroupResource;
import com.ericlam.mc.eld.components.GroupConfiguration;

import java.util.ArrayList;
import java.util.List;

@GroupResource(
        folder = "tables",
        preloads = { "mcinf", "gunsg" }
)
public class GameTable extends GroupConfiguration {

    public String table;
    public String timeColumn;
    public String uuidColumn;
    public List<String> statsColumns = new ArrayList<>();

}
