package com.mindaces.mindaces.controller;

import com.mindaces.mindaces.dto.UserDto;
import com.mindaces.mindaces.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/API")
public class SignUpAPIController
{
    UserService userService;
    SignUpAPIController(UserService userService)
    {
        this.userService = userService;
    }

    @PostMapping("/sendIDAPI")
    public String idCheck(Model model, UserDto userDto)
    {

        Long isDuplicateUser = 0L;
        String msg = "중복된";
        String idOrEmail = "아이디입니다";
        if(userDto.getUserID() == null)
        {
            idOrEmail = "이메일입니다";
            isDuplicateUser = userService.findUserEmail(userDto.getUserEmail());
        }
        else if(userDto.getUserEmail() == null)
        {
            isDuplicateUser = userService.findUserID(userDto.getUserID());
        }
        if(isDuplicateUser == -1L)
        {
            msg = "사용가능한";
        }
        model.addAttribute("msg",msg + " " + idOrEmail);
        return "userInfoPage/signup :: #resultArea";
    }
}
