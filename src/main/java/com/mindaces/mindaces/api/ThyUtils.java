package com.mindaces.mindaces.api;

import com.mindaces.mindaces.domain.entity.User;
import com.mindaces.mindaces.service.ObjService;
import com.mindaces.mindaces.service.RoleService;
import com.mindaces.mindaces.service.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

class UserObjUtil
{
    public String getRepresentObj(String username)
    {
        return username;
    }

}

class DateUtil
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