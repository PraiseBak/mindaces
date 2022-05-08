package com.mindaces.mindaces.controller;

import com.mindaces.mindaces.dto.BoardDto;
import com.mindaces.mindaces.service.BoardSearchService;
import com.mindaces.mindaces.service.BoardService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@AllArgsConstructor
@Slf4j
public class MainController
{
    private BoardService boardService;
    private HttpSession httpSession;
    private BoardSearchService boardSearchService;

    @GetMapping("/")
    public String main(Model model)
    {
        List<BoardDto> boardDtoList = boardSearchService.getMostLikelyBoardList();
        model.addAttribute("postList",boardDtoList);
        model.addAttribute("pagingMode","");
        model.addAttribute("page",1);
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
