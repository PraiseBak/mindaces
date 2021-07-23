package com.mindaces.mindaces.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;


@Getter
@Entity
@NoArgsConstructor
@Table(name = "GALLERY")
public class Gallery
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gallery_idx")
    private Long galleryIdx;

    @Column(name = "gallery_name")
    private String galleryName;

    @Column(name = "special_gallery")
    private Boolean specialGallery;

    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime galleryCreatedDate;

    @Builder
    public Gallery(Long galleryIdx,String galleryName,Boolean specialGallery)
    {
        this.galleryIdx = galleryIdx;
        this.galleryName = galleryName;
        this.specialGallery = specialGallery;
    }








}
