package com.mindaces.mindaces.service;

import com.mindaces.mindaces.domain.entity.Gallery;
import com.mindaces.mindaces.domain.repository.GalleryRepository;
import com.mindaces.mindaces.dto.GalleryDto;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GalleryService
{

    private GalleryRepository galleryRepository;

    public GalleryService(GalleryRepository galleryRepository)
    {
        this.galleryRepository = galleryRepository;
    }

    public List<GalleryDto> getGalleryList()
    {
        List<Gallery> galleries;
        List<GalleryDto> galleryDtoList = new ArrayList<>();

        galleries = galleryRepository.findAll();
        for(Gallery gallery : galleries)
        {
            galleryDtoList.add(this.convertEntityToDto(gallery));
        }

        return galleryDtoList;
    }

    private GalleryDto convertEntityToDto(Gallery gallery)
    {
        //필요한 부분만 엔티티로 만들때 dto에서 때오면 됨
        return GalleryDto.builder()
                .galleryIdx(gallery.getGalleryIdx())
                .galleryName(gallery.getGalleryName())
                .specialGallery(gallery.getSpecialGallery())
                .galleryCreatedTime(gallery.getGalleryCreatedDate())
                .build();
    }




}


