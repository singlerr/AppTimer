package kr.apptimer.database.utils;

import androidx.room.TypeConverter;

import java.util.Calendar;
import java.util.Date;

/***
 * Convert class of handling unsupported {@link java.util.Date} type
 * @author Singlerr
 */
public final class DateConverter {

    @TypeConverter
    public static Date toDate(Long dateLong){
        return dateLong == null ? null : new Date(dateLong);
    }

    @TypeConverter
    public static Long fromDate(Date date){
        return date == null ? null : date.getTime();
    }
}
