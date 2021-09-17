package com.mindaces.mindaces.controller;

import com.mindaces.mindaces.dto.BoardDto;
import com.mindaces.mindaces.dto.CommentDto;
import com.mindaces.mindaces.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//보여주거나 갤러리에 관련된것만 (board 수정하는건 BoardController)
@Controller
@RequestMapping(value = "/gallery")
public class GalleryController
{
    private CommentService commentService;
    private BoardService boardService;
    private GalleryService galleryService;
    private RoleService roleService;
    private LikeService likeService;

    String errorGalleryURL = "redirect:/error/GalleryError";
    String errorBoardURL = "redirect:/error/BoardError";

    public GalleryController(BoardService boardService, GalleryService galleryService, CommentService commentService,RoleService roleService,LikeService likeService)
    {
        this.commentService = commentService;
        this.boardService = boardService;
        this.galleryService = galleryService;
        this.roleService = roleService;
        this.likeService = likeService;
    }

    @GetMapping(value = "/galleryList" )
    public String galleryList(
            Model model)
    {
        model.addAttribute("galleryList",galleryService.getGalleryList());
        return "gallery/galleryList";
    }

    @GetMapping(value = "/{galleryName}" )
    public String galleryContentList(
            Model model,
            @RequestParam(required = false,defaultValue = "1") Integer page,
            @RequestParam(required = false,defaultValue = "",value = "pagingMode") String pagingMode,
            @PathVariable(name = "galleryName") String galleryName
    )
    {

        List<BoardDto> boardDtoList = new ArrayList<BoardDto>();
        Integer[] pageList;
        Boolean isGallery = galleryService.isGallery(galleryName);
        if(!isGallery)
        {
            return errorGalleryURL;
        }

        if(!(pagingMode.equals("")|| pagingMode.equals("mostLikedBoard")))
        {
            return errorBoardURL;
        }

        boardDtoList = boardService.getGalleryPost(galleryName,page);
        pageList = boardService.getPageList(galleryName,page);
        model.addAttribute("pageList",pageList);
        model.addAttribute("postList",boardDtoList);
        model.addAttribute("galleryName",galleryName);
        return "gallery/galleryContentList";

    }

    //글 내용 보여주기
    @GetMapping(value = "/{galleryName}/{index}")
    public String postContent(
            Model model,
            @PathVariable(name="galleryName") String galleryName,
            @PathVariable(name="index") Long contentIdx,
            Authentication authentication,
            @AuthenticationPrincipal OAuth2User principal
    )
    {
        Boolean isGallery;
        BoardDto boardDto;
        List<CommentDto> commentDtoList;
        String userName;
        String userPassword = "****";

        isGallery = galleryService.isGallery(galleryName);
        if(!isGallery)
        {
            return errorGalleryURL;
        }

        boardDto = boardService.getBoardByIdxAndGalleryName(galleryName,contentIdx);
        commentDtoList = commentService.getCommentByContentIdxAndGalleryName(galleryName,contentIdx);
        model.addAttribute("commentList",commentDtoList);
        if(principal != null)
        {
            userName = principal.getAttribute("name");
        }
        else
        {
            userName = roleService.getUserName(authentication);
        }

        if(userName.equals("-"))
        {
            userPassword = "";
            userName = "";
        }

        List<CommentDto> mostLikedCommentList = likeService.getMostLikedCommentList(commentDtoList);

        model.addAttribute("mostLikedCommentList",mostLikedCommentList);
        model.addAttribute("board",boardDto);
        model.addAttribute("userName",userName);
        model.addAttribute("userPassword",userPassword);
        return "gallery/postContent";
    }


}

