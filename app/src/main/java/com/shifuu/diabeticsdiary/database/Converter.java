package com.shifuu.diabeticsdiary.database;

import androidx.room.ProvidedTypeConverter;
import androidx.room.TypeConverter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class Converter {

    @TypeConverter
    public static LocalDate LocalDateFromTimestamp(Long value) {
        return value == null ? null : LocalDate.ofEpochDay(value);
    }

    @TypeConverter
    public static long LocalDateToTimestamp(LocalDate date) {
        return date.toEpochDay();
    }

    @TypeConverter
    public static long LocalDateTimeToTimestamp(LocalDateTime dateTime)
    {
        ZonedDateTime zdt = dateTime.atZone(ZoneId.systemDefault());
        return zdt.toInstant().toEpochMilli();
    }

    @TypeConverter
    public static LocalDateTime LocalDateTimeFromTimestamp (long value)
    {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(value), ZoneId.systemDefault());
    }
}
