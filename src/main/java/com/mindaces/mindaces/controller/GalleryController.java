package com.mindaces.mindaces.controller;

import com.mindaces.mindaces.dto.BoardDto;
import com.mindaces.mindaces.service.BoardService;
import com.mindaces.mindaces.service.GalleryService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

        System.out.println("갤러리이름: " + galleryName + "농");
        if(galleryName.equals("galleryList"))
        {
            model.addAttribute("galleryList",galleryService.getGalleryList());
            return "gallery/galleryList";
        }

        List<BoardDto> boardDtoList = boardService.getGalleryPost(galleryName);
        model.addAttribute("postList",boardDtoList);
        model.addAttribute("galleryName",boardDtoList.get(0).getGallery());
        return "gallery/galleryMain";
    }


    @GetMapping(value = "/{galleryName}/postWrite")
    public String postWrite(@PathVariable(name = "galleryName") String galleryName)
    {
        return "gallery/postWrite";
    }

    @PostMapping(value = "/{galleryName}/postWrite")
    public String postSubmit(@PathVariable(name = "galleryName") String galleryName, BoardDto boardDto)
    {
        //TODO checkValid()
        //존재하지 않는 갤러리라던가 등
        boardDto.setGallery(galleryName);
        System.out.println("pw:" + boardDto.getPassword());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boardDto.setPassword(passwordEncoder.encode(boardDto.getPassword()));
        boardService.savePost(boardDto);
        return "redirect:";
    }

    @GetMapping(value = "/{galleryName}/modify/{index}")
    public String modify(
            @PathVariable(name = "galleryName") String galleryName,
            @PathVariable(name = "index") Long contentIdx,
            Model model
            )
    {

        BoardDto boardDto = boardService.getBoardInfoByGalleryAndIdx(galleryName,contentIdx);
        boardDto.setPassword("****");
        model.addAttribute("board",boardDto);
        return "gallery/postWrite";
    }

    @PostMapping(value = "/{galleryName}/modify/postModify/{index}")
    public String postModify(
            @PathVariable(name = "galleryName") String galleryName,
            @PathVariable(name = "index") Long contentIdx,
            BoardDto boardDto
    )
    {

        boardDto.setContentIdx(contentIdx);
        Long result = boardService.updatePost(boardDto);
        System.out.println("result : " + result);
        return "redirect:/gallery/" + galleryName;
    }

    @GetMapping(value = "/{galleryName}/delete/{index}")
    public String deletePost(
            @PathVariable(name = "galleryName") String galleryName,
            @PathVariable(name = "index") Long contentIdx
    )
    {

        boardService.deletePost(contentIdx);
        return "redirect:/gallery/" + galleryName;
    }

}
