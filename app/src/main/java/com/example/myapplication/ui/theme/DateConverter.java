package com.example.myapplication.ui.theme;

import androidx.room.TypeConverter;

import java.util.Date;

// DateConverter for Room
public class DateConverter {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
