package com.ericlam.mc.mgquests.manager;

import com.ericlam.mc.mgquests.QuestException;
import com.ericlam.mc.mgquests.config.QuestObject;
import com.ericlam.mc.mgquests.db.Quest;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class DurationConvertManager {

    private final Map<String, Function<Long, Duration>> durationConverterMap = new HashMap<>();

    public DurationConvertManager() {
        durationConverterMap.put("minutes", Duration::ofMinutes);
        durationConverterMap.put("hours", Duration::ofHours);
        durationConverterMap.put("days", Duration::ofDays);
        durationConverterMap.put("months", num -> Duration.ofDays(num * 30));
        durationConverterMap.put("weeks", num -> Duration.ofDays(num * 7));
    }

    public Function<Long, Duration> getConverter(String unit) {
        return durationConverterMap.get(unit);
    }


    public LocalDateTime getDeadline(Quest quest, QuestObject.TimeDuration duration) throws QuestException {
        var converter = getConverter(duration.type);
        if (converter == null) throw new QuestException("time-limit-not-exist", duration.type);
        var time = converter.apply(duration.time);
        return new Timestamp(quest.lastStarted).toLocalDateTime().plus(time);
    }


    public LocalDateTime getCoolDownEndTime(Quest quest, QuestObject.TimeDuration duration) throws QuestException {
        var converter = getConverter(duration.type);
        if (converter == null) throw new QuestException("time-limit-not-exist", duration.type);
        var time = converter.apply(duration.time);
        return new Timestamp(quest.lastFinished).toLocalDateTime().plus(time);
    }

    public String formatDuration(Duration duration) {
        long seconds = duration.getSeconds();
        long absSeconds = Math.abs(seconds);
        return String.format(
                "%d:%02d:%02d",
                absSeconds / 3600,
                (absSeconds % 3600) / 60,
                absSeconds % 60);
    }
}
