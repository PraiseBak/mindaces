package com.mindaces.mindaces.controller;

import com.mindaces.mindaces.domain.entity.Board;
import com.mindaces.mindaces.dto.CommentDto;
import com.mindaces.mindaces.service.BoardService;
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
    private BoardService boardService;

    CommentController(CommentService commentService,BoardService boardService)
    {
        this.commentService = commentService;
        this.boardService = boardService;
    }


    @PostMapping(value = "/{galleryName}/{index}")
    public String commentAdd(
            @PathVariable(name="galleryName") String galleryName,
            @PathVariable(name="index") Long contentIdx,
            CommentDto commentDto,
            Authentication authentication
    )
    {
        Board board;
        commentService.addComment(galleryName,contentIdx,authentication,commentDto);
        board = boardService.getGalleryNameAndBoardIdx(galleryName, contentIdx);
        boardService.updateIsRecommendBoard(galleryName,contentIdx,board);
        return "redirect:" + contentIdx;
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
