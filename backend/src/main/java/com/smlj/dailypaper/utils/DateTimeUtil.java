package com.smlj.dailypaper.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Calendar;
import java.util.TimeZone;

@Slf4j
public final class DateTimeUtil {
    // 时间戳转换为当天0点的时间戳
    public static long convertToMidnightTimestamp(long timestamp) {
        var calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));

        // 设置时间为给定的时间戳
        calendar.setTimeInMillis(timestamp * 1000);

        // calendar.get(Calendar.MONTH)获取的月份比实际少1
        // log.info("{}, {}, {}", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));

        // 设置时间为当天0点
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // 返回当天0点的时间戳
        return calendar.getTimeInMillis() / 1000;
    }

    // 时间戳转换为当周0点的时间戳
    public static long convertToWeekMidnightTimestamp(long timestamp) {
        var calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));

        // 设置时间为给定的时间戳
        calendar.setTimeInMillis(timestamp * 1000);

        // Calendar 类中代表星期几的常量是从 Calendar.SUNDAY（值为 1）开始的，并没有 0 这个值。正确的使用方式应该是传入从 1 到 7 之间的数字，分别对应星期日到星期六。
        calendar.set(Calendar.DAY_OF_WEEK, 2);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // 返回当天0点的时间戳
        return calendar.getTimeInMillis() / 1000;
    }

    public static long plusDay(long timestamp, int days) {
        return timestamp + (long) days * 24 * 60 * 60;
    }

    public static long nowTimestamp() {
        return System.currentTimeMillis() / 1000;
    }
}
