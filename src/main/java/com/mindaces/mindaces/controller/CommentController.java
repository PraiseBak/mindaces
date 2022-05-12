package com.mindaces.mindaces.controller;

import com.mindaces.mindaces.domain.entity.Board;
import com.mindaces.mindaces.dto.CommentDto;
import com.mindaces.mindaces.service.*;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    private GalleryService galleryService;

    @Transactional
    @PostMapping(value = "/{galleryURL}/{index}")
    public String commentAdd(
            @PathVariable(name= "galleryURL") String galleryURL,
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
        String galleryName = galleryService.getGalleryNameByURL(galleryURL);
        result = commentService.addComment(galleryName,contentIdx,authentication,commentDto);
        board = boardSearchService.getGalleryNameAndBoardIdx(galleryName, contentIdx);
        boardInfoService.updateCommentCount(board);
        boardService.updateIsRecommendBoard(galleryName,contentIdx,board);
        attributes.addAttribute("pagingMode",pagingMode);
        attributes.addAttribute("page",page);
        return "redirect:" + contentIdx;
    }

    @Transactional
    @PostMapping(value = "/{galleryURL}/{index}/deleteComment")
    public String commentDelete(
            @PathVariable(name="galleryURL") String galleryURL,
            @PathVariable(name="index") Long contentIdx,
            CommentDto commentDto,
            Authentication authentication,
            @RequestParam(required = false,defaultValue = "board",value = "pagingMode") String pagingMode,
            @RequestParam(required = false,defaultValue = "1") Integer page,
            RedirectAttributes attributes
    )
    {
        Board board;
        String galleryName = galleryService.getGalleryNameByURL(galleryURL);
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
