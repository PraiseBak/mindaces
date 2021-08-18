package com.mindaces.mindaces.controller;


import com.mindaces.mindaces.domain.entity.Board;
import com.mindaces.mindaces.dto.BoardDto;
import com.mindaces.mindaces.dto.GalleryDto;
import com.mindaces.mindaces.dto.LikeDto;
import com.mindaces.mindaces.service.BoardService;
import com.mindaces.mindaces.service.GalleryService;
import com.mindaces.mindaces.service.LikeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequestMapping(value = "/gallery")
public class GalleryController
{
    BoardService boardService;
    GalleryService galleryService;

    public GalleryController(BoardService boardService, GalleryService galleryService)
    {
        this.boardService = boardService;
        this.galleryService = galleryService;
    }

    @GetMapping(value = "/{galleryName}/{index}")
    public String postContent(
            Model model,
            @PathVariable(name="galleryName") String galleryName,
            @PathVariable(name="index") Long contentIndex
    )
    {
        BoardDto boardDto = boardService.getBoardByIdx(contentIndex);
        model.addAttribute("board",boardDto);
        return "gallery/postContent";
    }

    @GetMapping(value = "/{galleryName}" )
    public String gallery(
            Model model,
            @PathVariable(name = "galleryName") String galleryName)
    {
        if(galleryName.equals("galleryList"))
        {
            model.addAttribute("galleryList",galleryService.getGalleryList());
            return "gallery/galleryList";
        }

        List<BoardDto> boardDtoList = boardService.getGalleryPost(galleryName);
        model.addAttribute("postList",boardDtoList);

        return "gallery/galleryMain";
    }

    @GetMapping(value = "/{galleryName}/postWrite")
    public String postWrite(@PathVariable(name = "galleryName") String galleryName)
    {

        System.out.println(galleryName + "갤러리 에서의 글쓰기 요청");
        return "gallery/postWrite";
    }

    @PostMapping(value = "/{galleryName}/postWrite")
    public String postSubmit(@PathVariable(name = "galleryName") String galleryName, BoardDto boardDto)
    {
        //TODO checkValid()
        boardDto.setGallery(galleryName);
        boardService.savePost(boardDto);
        return "redirect:";
    }







}
