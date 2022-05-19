package com.mindaces.mindaces.controller;


import com.mindaces.mindaces.dto.GalleryDto;
import com.mindaces.mindaces.service.GalleryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@RequestMapping(value = "/admin")
@Controller
@AllArgsConstructor
public class AdminController
{
    GalleryService galleryService;

    @GetMapping(value = "/main")
    public String adminMain()
    {
        return "admin/main";
    }

    @GetMapping(value = "/gallery")
    public String adminGallery()
    {
        return "admin/gallery";
    }

    @GetMapping(value = "/gallery/add")
    public String adminGalleryAdd()
    {
        return "admin/galleryAdd";
    }

    @PostMapping(value = "/gallery/add")
    public String adminGalleryAdd(GalleryDto galleryDto)
    {
        galleryService.galleryAdd(galleryDto);
        return "admin/galleryAdd";
    }

    @GetMapping(value = "/gallery/del")
    public String adminGalleryDel(Model model)
    {
        model.addAttribute("galleryList",galleryService.getGalleryList());
        return "admin/galleryDel";
    }

    @PostMapping(value = "/gallery/del")
    public String adminGalleryDel(@RequestParam List<Long> checkedList)
    {
        galleryService.delGallery(checkedList);
        return "admin/galleryDel";
    }
}
