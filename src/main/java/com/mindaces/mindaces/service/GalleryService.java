package com.mindaces.mindaces.service;

import com.mindaces.mindaces.domain.repository.GalleryRepository;
import org.springframework.stereotype.Service;

@Service
public class GalleryService
{
    private GalleryRepository galleryRepository;

    public GalleryService(GalleryRepository galleryRepository)
    {
        this.galleryRepository = galleryRepository;
    }


}
