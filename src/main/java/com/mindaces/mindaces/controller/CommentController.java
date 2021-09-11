package com.mindaces.mindaces.controller;

import com.mindaces.mindaces.dto.CommentDto;
import com.mindaces.mindaces.service.CommentService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/gallery")
public class CommentController
{
    private CommentService commentService;

    CommentController(CommentService commentService)
    {
        this.commentService = commentService;
    }


    @PostMapping(value = "/{galleryName}/{index}")
    public String commentAdd(
            @PathVariable(name="galleryName") String galleryName,
            @PathVariable(name="index") Long contentIdx,
            CommentDto commentDto,
            Authentication authentication
    )
    {
        commentService.addComment(galleryName,contentIdx,authentication,commentDto);
        return "redirect:" + Long.toString(contentIdx);
    }

    @PostMapping(value = "/{galleryName}/{index}/deleteComment")
    public String commentDelete(
            @PathVariable(name="galleryName") String galleryName,
            @PathVariable(name="index") Long contentIdx,
            CommentDto commentDto,
            Authentication authentication,
            String inputCommentPassword
    )
    {
        commentService.deleteComment(commentDto,authentication);
        return "redirect:./";
    }

    @PostMapping(value = "/{galleryName}/{index}/modifyComment")
    public String commentModify(
            @PathVariable(name="galleryName") String galleryName,
            @PathVariable(name="index") Long contentIdx,
            CommentDto commentDto,
            Authentication authentication
    )
    {
        commentService.modifyComment(commentDto,authentication);
        return "redirect:./";
    }



}
