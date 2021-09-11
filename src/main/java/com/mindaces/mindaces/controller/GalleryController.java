package com.mindaces.mindaces.controller;

import com.mindaces.mindaces.dto.BoardDto;
import com.mindaces.mindaces.dto.CommentDto;
import com.mindaces.mindaces.service.BoardService;
import com.mindaces.mindaces.service.CommentService;
import com.mindaces.mindaces.service.GalleryService;
import com.mindaces.mindaces.service.RoleService;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    String errorURL = "redirect:/error/galleryMiss";

    public GalleryController(BoardService boardService, GalleryService galleryService, CommentService commentService,RoleService roleService)
    {
        this.commentService = commentService;
        this.boardService = boardService;
        this.galleryService = galleryService;
        this.roleService = roleService;
    }

    @GetMapping(value = "/galleryList" )
    public String galleryList(
            Model model)
    {

        model.addAttribute("galleryList",galleryService.getGalleryList());
        return "gallery/galleryList";
    }

    //갤러리 목록 or 갤러리 메인 페이지
    @GetMapping(value = "/{galleryName}" )
    public String galleryContentList(
            Model model,
            @RequestParam(required = false,defaultValue = "1") Integer page,
            @PathVariable(name = "galleryName") String galleryName)
    {
        Boolean isGallery = galleryService.isGallery(galleryName);
        if(!isGallery)
        {
            return errorURL;
        }
        List<BoardDto> boardDtoList = boardService.getGalleryPost(galleryName,page);
        Integer[] pageList = boardService.getPageList(galleryName,page);


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
            Authentication authentication
    )
    {
        Boolean isGallery = galleryService.isGallery(galleryName);
        if(!isGallery)
        {
            return errorURL;
        }

        BoardDto boardDto = boardService.getBoardByIdxAndGalleryName(galleryName,contentIdx);
        List<CommentDto> commentDtoList = commentService.getCommentByContentIdxAndGalleryName(galleryName,contentIdx);

        model.addAttribute("board",boardDto);
        model.addAttribute("commentList",commentDtoList);
        String userName = roleService.getUserName(authentication);
        String userPassword = "****";
        if(userName.equals("-"))
        {
            userPassword = "";
            userName = "";
        }
        model.addAttribute("userName",userName);
        model.addAttribute("userPassword",userPassword);
        return "gallery/postContent";
    }


}

