package com.mindaces.mindaces.controller;


import com.mindaces.mindaces.dto.GalleryDto;
import com.mindaces.mindaces.service.GalleryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
        return "admin/main";
    }


    @GetMapping(value = "/gallery/del")
    public String adminGalleryDel()
    {
        return "admin/galleryDel";
    }
}
