package com.mindaces.mindaces.controller;

import com.mindaces.mindaces.dto.UserDto;
import com.mindaces.mindaces.service.SendEmailService;
import com.mindaces.mindaces.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping("/API")
public class SendEmailAPIController
{
    private UserService userService;
    private SendEmailService sendEmailService;

    @PostMapping(value = "/checkEmail")
    @ResponseBody
    public Boolean checkEmail(UserDto userDto)
    {
        Boolean isValid =  userService.isExistingEmailAndID(userDto);
        return isValid;
    }

    @PostMapping(value = "/sendEmail")
    @ResponseBody
    public Boolean sendEmail(UserDto userDto)
    {
        Boolean isValid;
        isValid =  userService.isExistingEmailAndID(userDto);
        if(!isValid)
        {
            return false;
        }

        isValid = sendEmailService.sendEmail(userDto);

        if(!isValid)
        {
            System.err.println("이메일 보내기 실패 " + userDto.getUserEmail());
        }
        return isValid;
    }
}

