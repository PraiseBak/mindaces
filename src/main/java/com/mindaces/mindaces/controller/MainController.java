package com.mindaces.mindaces.controller;

import com.mindaces.mindaces.dto.BoardDto;
import com.mindaces.mindaces.service.BoardService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class MainController
{
    private BoardService boardService;

    public MainController(BoardService boardService)
    {
        this.boardService = boardService;
    }

    @GetMapping("/")
    public String main(Model model)
    {
        //List<BoardDto> boardDtoList = boardService.getBoardList();
        //model.addAttribute("recommandBoard",boardDtoList);

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
