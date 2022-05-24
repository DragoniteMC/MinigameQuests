package com.ericlam.mc.mgquests.manager;

import com.dragonite.mc.dnmc.core.main.DragoniteMC;
import com.dragonite.mc.dnmc.core.managers.SQLDataSource;
import com.ericlam.mc.mgquests.config.GameTable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class TableManager {

    private static final String GAME_STATS_SELECTOR = "SELECT %s FROM %s WHERE `%s` = ? AND `%s` > ? GROUP BY uuid";

    private final SQLDataSource sqlDataSource;


    public TableManager() {
        this.sqlDataSource = DragoniteMC.getAPI().getSQLDataSource();
    }

    public Map<String, Double> getTableStats(UUID player, GameTable gameTable, Duration duration, List<String> stat) {

        var map = new HashMap<String, Double>();

        String selector = (stat == null ? gameTable.statsColumns : stat).stream().map(stats -> String.format("SUM(%s) as %s", stats, stats)).collect(Collectors.joining(", "));
        String statement = String.format(GAME_STATS_SELECTOR, selector, gameTable.table, gameTable.uuidColumn, gameTable.timeColumn);

        var time = Timestamp.valueOf(LocalDateTime.now().minus(duration)).getTime();

        try (Connection connection = sqlDataSource.getConnection(); PreparedStatement stmt = connection.prepareStatement(statement)) {
            stmt.setString(1, player.toString());
            stmt.setLong(2, time);
            var result = stmt.executeQuery();
            if (result.next()) {
                for (String stats : gameTable.statsColumns) {
                    map.put(stats, result.getDouble(stats));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return map;
    }


    public Map<String, Double> getTableStats(UUID player, GameTable gameTable, Duration duration) {
        return getTableStats(player, gameTable, duration, null);
    }
}
