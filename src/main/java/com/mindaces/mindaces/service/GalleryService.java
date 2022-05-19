package com.mindaces.mindaces.service;

import com.mindaces.mindaces.domain.entity.Gallery;
import com.mindaces.mindaces.domain.repository.GalleryRepository;
import com.mindaces.mindaces.dto.GalleryDto;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

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
//                .galleryCreatedTime(gallery.getGalleryCreatedDate())
                .galleryURL(gallery.getGalleryURL())
                .build();
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
        Gallery gallery = this.getGalleryByGalleryName(galleryName);
        return gallery.getRecommendStandard();
    }

    public void updateRecommendInfo(String galleryName,Long likes,Boolean isNewRecommended)
    {
        Gallery gallery = this.getGalleryByGalleryName(galleryName);
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


    private Gallery getGalleryByGalleryName(String galleryName)
    {
        return this.galleryRepository.findByGalleryName(galleryName);
    }

    public Long getCountRecommendedBoardByGalleryName(String galleryName)
    {
        return this.getGalleryByGalleryName(galleryName).getRecommendedBoardCount();
    }

    public void updateRecommendStandard(String galleryName)
    {
        Gallery gallery = this.getGalleryByGalleryName(galleryName);
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

    public Gallery galleryAdd(GalleryDto galleryDto)
    {
        Gallery gallery = Gallery.builder()
                .galleryName(galleryDto.getGalleryName())
                .galleryURL(galleryDto.getGalleryURL())
                .specialGallery(false)
                .build();
        return galleryRepository.save(gallery);

    }


    public void delGallery(List<Long> checkedList)
    {
        galleryRepository.deleteAllById(checkedList);
    }
}


