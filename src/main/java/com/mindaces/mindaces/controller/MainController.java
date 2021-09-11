package com.mindaces.mindaces.controller;

import com.mindaces.mindaces.dto.BoardDto;
import com.mindaces.mindaces.service.BoardService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.List;

@Controller
public class MainController
{
    private BoardService boardService;
    private HttpSession httpSession;


    public MainController(BoardService boardService,HttpSession httpSession)
    {
        this.httpSession = httpSession;
        this.boardService = boardService;
    }

    @GetMapping("/")
    public String main(Model model)
    {
        List<BoardDto> boardDtoList = boardService.getMostLikelyBoardList();
        model.addAttribute("mostLikedBoardList",boardDtoList);
        return "main/main";
    }

    @GetMapping("/post")
    public String post()
    {
        return "main/post";
    }

    @PostMapping("/post")
    public String write(BoardDto boardDto)
    {
        boardService.savePost(boardDto);
        return "redirect:/";
    }

    @GetMapping("/fail")
    public String fail()
    {
        return "except/fail";
    }


}
