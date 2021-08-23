package com.mindaces.mindaces.controller;

import com.mindaces.mindaces.domain.entity.Board;
import com.mindaces.mindaces.dto.BoardDto;
import com.mindaces.mindaces.dto.UserDto;
import com.mindaces.mindaces.service.BoardService;
import com.mindaces.mindaces.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/API")
public class APIController
{
    UserService userService;
    BoardService boardService;

    APIController(UserService userService,BoardService boardService)
    {
        this.userService= userService;
        this.boardService = boardService;
    }

    @PostMapping("/sendIDAPI")
    public String idCheck(Model model, UserDto userDto)
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

    @PostMapping("/checkBoardPasswordAPI")
    @ResponseBody
    public Boolean checkBoardPassword(BoardDto boardDto)
    {
        Long contentIdx = boardDto.getContentIdx();
        String inputPassword=  boardDto.getPassword();
        Boolean result = boardService.checkPassword(contentIdx,inputPassword);
        return result;
    }

    @PostMapping("/checkUserAPI")
    @ResponseBody
    public Boolean checkUser(BoardDto boardDto, Authentication authentication)
    {
        System.out.println("이쿠조!");

        if(authentication == null)
        {

        }
        else
        {
            if (authentication.getPrincipal() instanceof User)
            {
                System.out.println("유저있농");
                return true;
            }
        }
        return false;
    }

}
