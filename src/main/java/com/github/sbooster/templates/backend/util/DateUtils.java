package com.github.sbooster.templates.backend.util;

import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

@Component
public class DateUtils {
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @SneakyThrows
    public static Date dateOf(short year, short month, short day) {
        String stringBirthDate = String.format("%s-%s-%s", year, month, day);
        return DATE_FORMAT.parse(stringBirthDate);
    }

    public static TemporalAccessor dateTimeOf(short year, short month, short day, short hour, short minute, short second) {
        String stringBirthDate = String.format("%s-%s-%s %s:%s:%s", year, month, day, hour, minute, second);
        return DATE_TIME_FORMATTER.parse(stringBirthDate);
    }

    @Bean
    public SimpleDateFormat getDateFormatter() {
        return DATE_FORMAT;
    }

    @Bean
    public DateTimeFormatter getDateTimeFormatter() {
        return DATE_TIME_FORMATTER;
    }
}
