package com.mindaces.mindaces.controller;

import com.mindaces.mindaces.dto.CommentDto;
import com.mindaces.mindaces.service.CommentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/API")
public class CommentAPIController
{
    CommentService commentService;

    CommentAPIController(CommentService commentService)
    {
        this.commentService = commentService;
    }

    @PostMapping("/checkCommentValidAPI")
    @ResponseBody
    public Boolean checkCommentValidAPI(CommentDto commentDto)
    {
        Boolean result = commentService.commentValidCheck(commentDto);
        return result;
    }

    @PostMapping("/checkCommentPasswordAPI")
    @ResponseBody
    public Boolean checkComentPasswordAPI(CommentDto commentDto)
    {
        if(commentService.getMatchPasswordComment(commentDto) != null)
        {
            return true;
        }
        return false;
    }
}
