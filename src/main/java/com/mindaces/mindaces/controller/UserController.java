package com.mindaces.mindaces.controller;

import com.mindaces.mindaces.dto.UserDto;
import com.mindaces.mindaces.service.*;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@AllArgsConstructor
public class UserController
{
    private RoleService roleService;
    private UserService userService;
    private BoardService boardService;
    private CommentService commentService;
    private BoardSearchService boardSearchService;

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

    @GetMapping(value = {"/user/{username}","/user/{username}/board"})
    public String boardWroteByUser(
            @PathVariable(name="username") String username,
            @RequestParam(required = false,defaultValue = "1") Integer page,
            Model model
    )
    {
        Long result = userService.findUserID(username);

        if(result != -1L)
        {
            boardSearchService.addingPagedBoardToModelByWritedUser(model,page,"board",username);
            model.addAttribute("selectedMode","Board");
            model.addAttribute("username",username);
       }
        model.addAttribute("pagingMode","board");
        model.addAttribute("page",page);

        return "userInfopage/userWroteContent";
    }

    @GetMapping("/user/{username}/recommendedBoard")
    public String recommendedBoardWroteByUser(
            @PathVariable(name="username") String username,
            @RequestParam(required = false,defaultValue = "1") Integer page,
            Model model
    )
    {
        Long result = userService.findUserID(username);
        if(result != -1L)
        {
            boardSearchService.addingPagedBoardToModelByWritedUser(model,page,"mostLikedBoard",username);
            model.addAttribute("selectedMode","LikedBoard");
            model.addAttribute("username",username);

        }
        model.addAttribute("pagingMode","board");
        model.addAttribute("page",page);
        return "userInfopage/userWroteContent";
    }

    @GetMapping("/user/{username}/comment")
    public String commentWroteByUser(
            @PathVariable(name="username") String username,
            @RequestParam(required = false,defaultValue = "1") Integer page,
            Model model
    )
    {
        Long result = userService.findUserID(username);

        if(result != -1L)
        {
            commentService.addingPagedCommentToModelByWritedUser(model,page,username);
            model.addAttribute("selectedMode","Comment");
            model.addAttribute("username",username);
        }

        model.addAttribute("pagingMode","board");
        model.addAttribute("page",page);
        return "userInfopage/userWroteContent";
    }

    @PostMapping("/user/findUserWroteContent")
    public String findUser(HttpServletRequest request)
    {
        String searchUsername = request.getParameter("searchUsername");
        return "redirect:/user/" + searchUsername;
    }

    @GetMapping("/user/userInfo/findPassword")
    public String findUserInfo()
    {
        return "userInfopage/findUserInfo";
    }

    @PostMapping("/user/userInfo/findPassword")
    public String findUserIDResult(
            Model model,
            HttpServletRequest request
    )
    {
        String userEmail;
        String resultUserID;
        userEmail = (String) request.getParameter("userEmail");
        System.out.println(userEmail);
        return "userInfopage/findUserInfoResult";
    }


    @GetMapping("/user/{username}/changePassword")
    public String changePassword(
            Model model,
            @PathVariable String username,
            Authentication authentication
    )
    {
        Boolean isSameUser;
        isSameUser = roleService.isSameUser(authentication,username);

        if(!isSameUser)
        {
            return "redirect:/error/roleError";
        }

        model.addAttribute("username",username);
        return "userInfopage/changeUserPassword";
    }


    @PostMapping("/user/{username}/changePassword")
    public String changePasswordResult(
            Model model,
            @PathVariable String username,
            Authentication authentication,
            HttpServletRequest request
    )
    {
        Boolean isSameUser;
        String originPassword;
        String newPasswordOne;
        String newPasswordTwo;
        Boolean changeResult;

        isSameUser = roleService.isSameUser(authentication,username);

        if(!isSameUser)
        {
            return "redirect:/error/roleError";
        }

        /*
        이거 공부
        Enumeration<String> e = request.getAttributeNames();
        while(e.hasMoreElements()) {
            System.out.println(e.nextElement());
        }
         */

        originPassword = (String) request.getParameter("originUserPassword");
        newPasswordOne = (String) request.getParameter("objUserPasswordOne");
        newPasswordTwo = (String) request.getParameter("objUserPasswordTwo");

        changeResult = userService.changePassword(originPassword,newPasswordOne,newPasswordTwo,username);

        if(changeResult)
        {
            return "redirect:/user/logout";
        }
        else
        {
            model.addAttribute("changeResult","유효하지 않은 시도입니다");
        }
        return "userInfopage/changeUserPassword";
    }


}
