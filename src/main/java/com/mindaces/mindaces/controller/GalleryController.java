package com.mindaces.mindaces.controller;

import com.mindaces.mindaces.dto.BoardDto;
import com.mindaces.mindaces.dto.CommentDto;
import com.mindaces.mindaces.service.*;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//보여주거나 갤러리에 관련된것만 (board 수정하는건 BoardController)
@Controller
@AllArgsConstructor
@RequestMapping(value = "/gallery")
public class GalleryController
{
    private UserService userService;
    private CommentService commentService;
    private BoardService boardService;
    private GalleryService galleryService;
    private RoleService roleService;
    private LikesService likesService;
    private BoardSearchService boardSearchService;

    final String errorGalleryURL = "redirect:/error/GalleryError";
    final String errorBoardURL = "redirect:/error/BoardError";


    @GetMapping(value = "/galleryList" )
    public String galleryList(
            Model model)
    {
        model.addAttribute("galleryList",galleryService.getGalleryList());
        return "gallery/galleryList";
    }

    @GetMapping(value = "/{galleryURL}")
    public String galleryContentList(
            Model model,
            @RequestParam(required = false,defaultValue = "board",value = "pagingMode") String pagingMode,
            @RequestParam(required = false,defaultValue = "1") Integer page,
            @PathVariable(name = "galleryURL") String galleryURL
    )
    {
        //갤러리 여부 판단
        Boolean isGallery = galleryService.isGallery(galleryURL);
        if(!isGallery)
        {
            //에러 출력 URL로 이동
            return errorGalleryURL;
        }

        //페이징 모드를 체크하고 에러 출력
        if(!(pagingMode.equals("board") || pagingMode.equals("mostLikedBoard")))
        {
            return errorBoardURL;
        }

        //페이징을 모델에 추가해주는 부분
        boardSearchService.addingPagedBoardToModel(model,galleryURL,page,pagingMode);

        String galleryName = galleryService.getGalleryNameByURL(galleryURL);

        model.addAttribute("galleryName",galleryURL);
        model.addAttribute("pagingMode",pagingMode);
        model.addAttribute("page",page);
        return "gallery/galleryContentList";
    }

    @RequestMapping(value = "/{galleryName}/search/{pagingMode}/{page}",method = {RequestMethod.GET})
    public String gallerySearch(
            Model model,
            @PathVariable(name = "galleryName") String galleryName,
            @PathVariable(name = "pagingMode") String pagingMode,
            @PathVariable(name = "page") Integer page,
            @RequestParam("searchKeyword") String searchKeyword,
            @RequestParam("searchMode") String searchMode,
            @RequestParam(value = "searchGalleryKeyword",required = false) String searchGalleryKeyword
    )
    {
        Boolean isGallery = galleryService.isGallery(galleryName);
        if(!isGallery)
        {
            return errorGalleryURL;
        }

        if(! (pagingMode.equals("board") || pagingMode.equals("mostLikedBoard")))
        {
            return errorBoardURL;
        }

        if(searchGalleryKeyword != null && searchGalleryKeyword.equals(""))
        {
            searchGalleryKeyword = null;
        }

        boardSearchService.addingPagedBoardToModelByKeyword(model,searchKeyword,searchMode,page,pagingMode,searchGalleryKeyword);
        model.addAttribute("galleryName",galleryName);
        model.addAttribute("pagingMode",pagingMode);
        model.addAttribute("page",page);
        model.addAttribute("searchKeyword",searchKeyword);
        model.addAttribute("searchMode",searchMode);
        model.addAttribute("searchGalleryKeyword",searchGalleryKeyword);
        return "gallery/galleryContentList";
    }

    //글 내용 보여주기
    @GetMapping(value = "/{galleryName}/{index}")
    public String postContent(
            Model model,
            @RequestParam(required = false,defaultValue = "board",value = "pagingMode") String pagingMode,
            @RequestParam(required = false,defaultValue = "1") Integer page,
            @RequestParam(required = false,defaultValue = "1") Integer commentPage,
            @PathVariable(name="galleryName") String galleryName,
            @PathVariable(name="index") Long contentIdx,
            Authentication authentication,
            @AuthenticationPrincipal OAuth2User principal
    )
    {
        Boolean isGallery;
        BoardDto boardDto;
        List<CommentDto> commentDtoList;
        List<CommentDto> mostLikedCommentList;
        String username;
        String userPassword = "****";

        isGallery = galleryService.isGallery(galleryName);
        if(!isGallery)
        {
            return errorGalleryURL;
        }

        boardDto = boardSearchService.getBoardDtoByGalleryAndIdx(galleryName,contentIdx);
        if(principal != null)
        {
            username = principal.getAttribute("name");
        }
        else
        {
            username = roleService.getUsername(authentication);
        }

        if(username.equals("-"))
        {
            userPassword = "";
            username = "";
        }

        boardSearchService.addingPagedBoardToModel(model,galleryName,page,pagingMode);
        commentService.addingPagedCommentToModel(model,galleryName,contentIdx,commentPage);
        mostLikedCommentList = likesService.getMostLikedCommentList((List<CommentDto>) model.getAttribute("commentList"));
        boardService.addVisitedNum(contentIdx);

        model.addAttribute("mostLikedCommentList",mostLikedCommentList);
        model.addAttribute("board",boardDto);
        model.addAttribute("username",username);
        model.addAttribute("userPassword",userPassword);
        model.addAttribute("pagingMode",pagingMode);
        model.addAttribute("page",page);

        return "gallery/postContent";
    }




}

