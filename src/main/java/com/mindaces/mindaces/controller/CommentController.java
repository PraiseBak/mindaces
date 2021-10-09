package com.mindaces.mindaces.controller;

import com.mindaces.mindaces.domain.entity.Board;
import com.mindaces.mindaces.dto.CommentDto;
import com.mindaces.mindaces.service.BoardInfoService;
import com.mindaces.mindaces.service.BoardSearchService;
import com.mindaces.mindaces.service.BoardService;
import com.mindaces.mindaces.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.transaction.Transactional;

@Controller
@AllArgsConstructor
@RequestMapping(value = "/gallery")
public class CommentController
{
    private CommentService commentService;
    private BoardService boardService;
    private BoardInfoService boardInfoService;
    private BoardSearchService boardSearchService;

    @Transactional
    @PostMapping(value = "/{galleryName}/{index}")
    public String commentAdd(
            @PathVariable(name="galleryName") String galleryName,
            @PathVariable(name="index") Long contentIdx,
            CommentDto commentDto,
            Authentication authentication,
            @RequestParam(required = false,defaultValue = "board",value = "pagingMode") String pagingMode,
            @RequestParam(required = false,defaultValue = "1") Integer page,
            RedirectAttributes attributes
    )
    {
        Board board;
        Boolean result;
        result = commentService.addComment(galleryName,contentIdx,authentication,commentDto);
        board = boardSearchService.getGalleryNameAndBoardIdx(galleryName, contentIdx);
        boardInfoService.updateCommentCount(board);
        boardService.updateIsRecommendBoard(galleryName,contentIdx,board);
        attributes.addAttribute("pagingMode",pagingMode);
        attributes.addAttribute("page",page);
        return "redirect:" + contentIdx;
    }

    @Transactional
    @PostMapping(value = "/{galleryName}/{index}/deleteComment")
    public String commentDelete(
            @PathVariable(name="galleryName") String galleryName,
            @PathVariable(name="index") Long contentIdx,
            CommentDto commentDto,
            Authentication authentication,
            @RequestParam(required = false,defaultValue = "board",value = "pagingMode") String pagingMode,
            @RequestParam(required = false,defaultValue = "1") Integer page,
            RedirectAttributes attributes
    )
    {
        Board board;
        board = boardSearchService.getGalleryNameAndBoardIdx(galleryName, contentIdx);
        commentService.deleteComment(commentDto,authentication);
        boardInfoService.updateCommentCount(board);
        attributes.addAttribute("pagingMode",pagingMode);
        attributes.addAttribute("page",page);
        return "redirect:../" + contentIdx;
    }
    /*
    @PostMapping(value = "/{galleryName}/{index}/modifyComment")
    public String commentModify(
            @PathVariable(name="galleryName") String galleryName,
            @PathVariable(name="index") Long contentIdx,
            CommentDto commentDto,
            Authentication authentication,
            @RequestParam(required = false,defaultValue = "board",value = "pagingMode") String pagingMode,
            @RequestParam(required = false,defaultValue = "1") Integer page,
            RedirectAttributes attributes

    )
    {
        commentService.modifyComment(commentDto,authentication);
        attributes.addAttribute("pagingMode",pagingMode);
        attributes.addAttribute("page",page);
        return "redirect:../" + contentIdx;
    }
     */
    
}
