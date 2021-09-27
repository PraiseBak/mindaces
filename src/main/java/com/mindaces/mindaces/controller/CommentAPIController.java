package com.mindaces.mindaces.controller;

import com.mindaces.mindaces.dto.CommentDto;
import com.mindaces.mindaces.service.CommentService;
import com.mindaces.mindaces.service.LikesService;
import com.mindaces.mindaces.service.RoleService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/API")
public class CommentAPIController
{
    LikesService likesService;
    CommentService commentService;
    RoleService roleService;

    CommentAPIController(CommentService commentService, RoleService roleService, LikesService likesService)
    {
        this.likesService = likesService;
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

    @PostMapping("/requestCommentRecommendAPI")
    @ResponseBody
    public String recommendCommentRequestAPI(@RequestParam Map<String, Object> map, HttpServletRequest request, Authentication authentication)
    {
        String result = this.likesService.commentRecommand(map, request,authentication);
        return result;
    }

    @PostMapping("/getRenewCommentLikesAPI")
    @ResponseBody
    public Map<String,Long> getRecentCommentLikesAPI(@RequestParam(value = "commentIdx") Long commentIdx)
    {
        return this.likesService.getRecentCommentLikes(commentIdx);
    }


}
