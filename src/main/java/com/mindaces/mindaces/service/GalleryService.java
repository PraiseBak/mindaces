package com.mindaces.mindaces.service;

import com.mindaces.mindaces.domain.repository.BoardRepository;
import com.mindaces.mindaces.domain.repository.GalleryRepository;
import com.mindaces.mindaces.dto.GalleryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

@Service
public class GalleryService
{

    private GalleryRepository galleryRepository;

    public GalleryService(GalleryRepository galleryRepository)
    {
        this.galleryRepository =galleryRepository;
    }



}
