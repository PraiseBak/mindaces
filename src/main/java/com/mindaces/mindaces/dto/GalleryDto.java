package com.mindaces.mindaces.dto;

import com.mindaces.mindaces.domain.entity.Gallery;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class GalleryDto
{
    private Long galleryIdx;

    private String galleryName;

    private Boolean specialGallery;

    private LocalDateTime galleryCreatedDate;

    public Gallery toEntity()
    {
        Gallery gallery = Gallery.builder()
                .galleryIdx(galleryIdx)
                .galleryName(galleryName)
                .specialGallery(specialGallery)
                .build();
        return gallery;
    }

    @Builder
    public GalleryDto(Long galleryIdx,String galleryName,Boolean specialGallery,LocalDateTime galleryCreatedTime)
    {
        this.galleryIdx = galleryIdx;
        this.galleryName = galleryName;
        this.specialGallery = specialGallery;
    }


}
