package com.mindaces.mindaces.controller;


import com.mindaces.mindaces.dto.BoardDto;
import com.mindaces.mindaces.dto.GalleryDto;
import com.mindaces.mindaces.service.BoardService;
import com.mindaces.mindaces.service.GalleryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
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
        GalleryDto galleryInfo = galleryService.getGalleryInfo(galleryName);
        model.addAttribute("postList",boardDtoList);
        model.addAttribute("galleryInfo",galleryInfo);
        return "gallery/galleryMain";
    }



}
