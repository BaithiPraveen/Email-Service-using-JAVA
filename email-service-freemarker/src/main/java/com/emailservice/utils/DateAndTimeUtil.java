package com.emailservice.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateAndTimeUtil {

    public static String getDateAndTime(){
        return  LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }
}
