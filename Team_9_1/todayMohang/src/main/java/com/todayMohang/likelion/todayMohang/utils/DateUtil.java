package com.todayMohang.likelion.todayMohang.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    public static LocalDateTime parse(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate localDate = LocalDate.parse(dateString, formatter);

        return LocalDateTime.of(localDate, LocalTime.MIDNIGHT);
    }

}
