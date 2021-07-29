package com.mindaces.mindaces.controller;


import com.mindaces.mindaces.domain.entity.User;
import com.mindaces.mindaces.dto.UserDto;
import com.mindaces.mindaces.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@AllArgsConstructor
public class UserController
{
    private UserService userService;

    @GetMapping("/user/signup")
    public String userSignUp()
    {
        return "/signup";
    }

    @PostMapping("/sendIDAPI")
    public String idCheck(Model model,UserDto userDto)
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
        if(idOrEmail.equals("이메일입니다"))
        {
            return "/signup :: #resultArea";
        }
        return "/signup :: #alarmArea";
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
        return "/login";
    }

    @PostMapping("/")
    public String userlogin(UserDto usertDto)
    {
        userService.loadUserByUsername(usertDto.getUserID());
        return "/";
    }

    @GetMapping("/user/login/result")
    public String loginResult()
    {
        return "/loginSuccess";
    }

    @GetMapping("/user/logout/result")
    public String logout()
    {
        return "/logout";
    }

    @GetMapping("/user/denied")
    public String denied()
    {
        return "/denied";
    }

    @GetMapping("/user/info")
    public String userInfo()
    {
        return "/myinfo";
    }

    @GetMapping("/admin")
    public String admin()
    {
        return "/admin";
    }


}
