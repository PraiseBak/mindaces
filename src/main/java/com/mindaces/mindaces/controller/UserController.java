package com.mindaces.mindaces.controller;

import com.mindaces.mindaces.dto.UserDto;
import com.mindaces.mindaces.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@AllArgsConstructor
public class UserController
{

    private UserService userService;

    @GetMapping("/user/signup")
    public String userSignUp()
    {
        return "userInfoPage/signup";
    }

    @PostMapping("/sendIDAPI")
    public String idCheck(Model model,UserDto userDto)
    {
        Long isDuplicateUser = 0L;
        String msg = "중복된";
        String idOrEmail = "닉네임입니다";
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
        if(idOrEmail.equals("이메일입니다"))
        {
            return "userInfoPage/signup :: #resultArea";
        }
        return "userInfoPage/signup :: #alarmArea";
    }


    @PostMapping("/user/signup")
    public String execSignup(UserDto userDto)
    {
        if(userService.joinUser(userDto) == -1)
        {
            return "redirect:/fail";
        }
        return "redirect:/";
    }

    @GetMapping("/user/login")
    public String userlogin()
    {
        return "userInfoPage/login";
    }
/*
    @PostMapping("/user/login")
    public String userlogin(HttpServletRequest req,UserDto userDto)
    {

        //SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
        userService.loadUserByUsername(userDto.getUserID());

        return "redirect:/";
    }
*/

    @GetMapping("/user/login/result")
    public String loginResult()
    {
        return "userInfoPage/loginSuccess";
    }

    @GetMapping("/user/logout/result")
    public String logout()
    {
        return "userInfoPage/logout";
    }


    @GetMapping("/user/denied")
    public String dispDenied() {
        return "userInfoPage/denied";
    }

    @GetMapping("/user/myinfo")
    public String userInfo()
    {
        return "userInfoPage/myinfo";
    }

    @GetMapping("/admin")
    public String admin()
    {
        return "userInfoPage/admin";
    }

    @GetMapping("/user/selectSignup")
    public String beforeSignup()
    {
        return "userInfoPage/selectSignup";

    }


}
