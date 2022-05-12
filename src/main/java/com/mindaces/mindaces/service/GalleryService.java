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
    //이 카운트를 넘어가야 평균치로 개념글 기준 잡음
    private final int renewRecommendeBoardCount = 5;
    private final int initRecommendStandard = 10;

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


    public GalleryDto getGalleryInfo(String galleryURL)
    {
        Gallery gallery = galleryRepository.findByGalleryURL(galleryURL);
        GalleryDto galleryDto = this.convertEntityToDto(gallery);
        return galleryDto;
    }

    public Boolean isGallery(String galleryURL)
    {
        Gallery gallery = galleryRepository.findByGalleryURL(galleryURL);
        if(gallery == null)
        {
            return false;
        }
        return true;
    }

    public Long getRecommendStandard(String galleryURL)
    {
        Gallery gallery = this.getGalleryByGalleryURL(galleryURL);
        return gallery.getRecommendStandard();
    }

    public void updateRecommendInfo(String galleryURL,Long likes,Boolean isNewRecommended)
    {
        Gallery gallery = this.getGalleryByGalleryURL(galleryURL);
        if(isNewRecommended)
        {
            gallery.addRecommendedLikesSum(likes);
            gallery.updateRecommendBoardCount();
        }
        else
        {
            gallery.addRecommendedLikesSum(1L);
        }
    }


    private Gallery getGalleryByGalleryURL(String galleryURL)
    {
        return this.galleryRepository.findByGalleryURL(galleryURL);
    }

    public Long getCountRecommendedBoardByGalleryURL(String galleryURL)
    {
        return this.getGalleryByGalleryURL(galleryURL).getRecommendedBoardCount();
    }

    public void updateRecommendStandard(String galleryURL)
    {
        Gallery gallery = this.getGalleryByGalleryURL(galleryURL);
        Long recommendedBoardCount = gallery.getRecommendedBoardCount();
        Long recommendedLikesSum = gallery.getRecommendedLikesSum();
        if(recommendedBoardCount < 10)
        {
            return;
        }

        Long newRecommendedStandard = recommendedLikesSum / recommendedBoardCount;

        if(recommendedBoardCount > this.renewRecommendeBoardCount)
        {
            gallery.updateRecommendStandard(newRecommendedStandard);
            galleryRepository.save(gallery);
        }

    }

    public String getGalleryNameByURL(String galleryURL)
    {
        return this.galleryRepository.findByGalleryURL(galleryURL).getGalleryName();
    }
}


