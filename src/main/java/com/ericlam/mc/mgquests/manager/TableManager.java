package com.ericlam.mc.mgquests.manager;

import com.dragonite.mc.dnmc.core.main.DragoniteMC;
import com.dragonite.mc.dnmc.core.managers.SQLDataSource;
import com.ericlam.mc.eld.misc.DebugLogger;
import com.ericlam.mc.eld.services.LoggingService;
import com.ericlam.mc.mgquests.config.GameTable;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class TableManager {

    private static final String GAME_STATS_SELECTOR = "SELECT %s FROM %s WHERE `%s` = ? AND `%s` BETWEEN ? AND ? GROUP BY uuid";

    private final SQLDataSource sqlDataSource;


    private final DebugLogger logger;


    @Inject
    public TableManager(LoggingService loggingService) {
        this.sqlDataSource = DragoniteMC.getAPI().getSQLDataSource();
        this.logger = loggingService.getLogger(TableManager.class);
    }

    public Map<String, Double> getTableStats(UUID player, GameTable gameTable, Duration duration, Set<String> stat, long lastStarted) {

        var map = new HashMap<String, Double>();
        var toFetchedStats = (stat == null ? gameTable.statsColumns : stat);
        String selector = toFetchedStats.stream().map(stats -> String.format("SUM(%s) as %s", stats, stats)).collect(Collectors.joining(", "));
        String statement = String.format(GAME_STATS_SELECTOR, selector, gameTable.table, gameTable.uuidColumn, gameTable.timeColumn);

        var deadline =  Timestamp.valueOf(new Timestamp(lastStarted).toLocalDateTime().plus(duration)).getTime();

        logger.debugF("preparing to execute statement: %s", statement);
        logger.debugF("with parameters: %s, %s, %s", player.toString(), lastStarted, deadline);

        try (Connection connection = sqlDataSource.getConnection(); PreparedStatement stmt = connection.prepareStatement(statement)) {
            stmt.setString(1, player.toString());
            stmt.setLong(2, lastStarted);
            stmt.setLong(3, deadline);
            var result = stmt.executeQuery();
            if (result.next()) {
                for (String stats : toFetchedStats) {
                    map.put(stats, result.getDouble(stats));
                }
                logger.debugF("successfully fetched stats: %s", map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return map;
    }


    public Map<String, Double> getTableStats(UUID player, GameTable gameTable, Duration duration, long lastStarted) {
        return getTableStats(player, gameTable, duration, null, lastStarted);
    }
}
