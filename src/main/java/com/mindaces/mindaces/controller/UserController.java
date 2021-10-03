package com.mindaces.mindaces.controller;

import com.mindaces.mindaces.dto.UserDto;
import com.mindaces.mindaces.service.BoardService;
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
    private BoardService boardService;


    @GetMapping("/user/signup")
    public String userSignUp()
    {
        return "userInfoPage/signup";
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


    // Login form with error
    @RequestMapping("/loginError.html")
    public String loginError(Model model) {
        model.addAttribute("errorMsg", "로그인에 실패하였습니다.");
        return "userInfoPage/login";
    }

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
        return "mypage";
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

    @GetMapping("/user/{username}")
    public String mypage(
            @PathVariable(name="username") String username,
            @RequestParam(required = false,defaultValue = "",value = "pagingMode") String pagingMode,
            @RequestParam(required = false,defaultValue = "1") Integer page,
            Model model
    )
    {
        boardService.addingPagedBoardToModelByWritedUser(model,page,pagingMode,username);

        return "userInfoPage/mypage";
    }




}
