package com.mindaces.mindaces.service;

import com.mindaces.mindaces.domain.entity.Gallery;
import com.mindaces.mindaces.domain.repository.GalleryRepository;
import com.mindaces.mindaces.dto.GalleryDto;
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
                .galleryURL(gallery.getGalleryURL())
                .build();
    }


    public GalleryDto getGalleryInfo(String galleryName)
    {
        Gallery gallery = galleryRepository.findByGalleryName(galleryName);
        GalleryDto galleryDto = this.convertEntityToDto(gallery);
        return galleryDto;
    }

    public Boolean isGallery(String galleryName)
    {
        Gallery gallery = galleryRepository.findByGalleryName(galleryName);
        if(gallery == null)
        {
            return false;
        }
        return true;
    }

    public Long getRecommendStandard(String galleryName)
    {
        Gallery gallery = this.galleryRepository.findByGalleryName(galleryName);
        return gallery.getRecommendStandard();
    }


    public void refreshRecommendStandard(String galleryName,Long recommendStandard)
    {
        Gallery gallery = this.galleryRepository.findByGalleryName(galleryName);
        gallery.setRecommendStandard(recommendStandard);
        this.galleryRepository.save(gallery);

    }
}


