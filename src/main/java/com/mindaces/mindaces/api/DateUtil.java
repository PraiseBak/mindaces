package com.mindaces.mindaces.api;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class DateUtil
{
    public String dateSubtract(LocalDateTime localDateTime, Integer objDay)
    {
        LocalDateTime curTime = LocalDateTime.now();
        Integer curDay = Integer.parseInt(ChronoUnit.DAYS.between(localDateTime, curTime) + "");
        Integer result = objDay - curDay;
        if (result < 1)
        {
            return "목표일 달성! 🎉";
        }
        return result + "";
    }

    public String dateBetween(LocalDateTime localDateTime, Integer objDay)
    {
        LocalDateTime curTime = LocalDateTime.now();
        Integer curDay = Integer.parseInt(ChronoUnit.DAYS.between(localDateTime, curTime) + "");
        return curDay +"";
    }
}