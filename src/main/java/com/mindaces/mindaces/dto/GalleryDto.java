package com.mindaces.mindaces.dto;

import com.mindaces.mindaces.domain.entity.Gallery;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class GalleryDto
{
    private Long galleryIdx;

    private String galleryName;

    private String galleryURL;

    private Boolean specialGallery;

    private LocalDateTime galleryCreatedDate;


    @Builder
    public GalleryDto(Long galleryIdx,String galleryName,String galleryURL,Boolean specialGallery)
    {
        this.galleryIdx = galleryIdx;
        this.galleryName = galleryName;
        this.specialGallery = specialGallery;
        this.galleryURL = galleryURL;
    }


}
