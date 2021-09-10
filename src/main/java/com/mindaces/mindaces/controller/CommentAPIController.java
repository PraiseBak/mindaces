package com.mindaces.mindaces.controller;

import com.mindaces.mindaces.dto.CommentDto;
import com.mindaces.mindaces.service.CommentService;
import com.mindaces.mindaces.service.RoleService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/API")
public class CommentAPIController
{
    CommentService commentService;
    RoleService roleService;

    CommentAPIController(CommentService commentService, RoleService roleService)
    {
        this.commentService = commentService;
        this.roleService = roleService;
    }

    @PostMapping("/checkCommentValidAPI")
    @ResponseBody
    public Boolean checkCommentValidAPI(CommentDto commentDto)
    {
        Boolean result = commentService.addCommentValidCheck(commentDto);
        return result;
    }

    @PostMapping("/checkCommentPasswordAPI")
    @ResponseBody
    public Boolean checkCommentPasswordAPI(CommentDto commentDto)
    {
        if(commentService.getMatchPasswordComment(commentDto) != null)
        {
            return true;
        }
        return false;
    }

    @PostMapping("/checkCommentUserAPI")
    @ResponseBody
    public Boolean checkCommentUserAPI(CommentDto commentDto, Authentication authentication)
    {
        return commentService.isSameUser(commentDto,authentication);
    }

}
