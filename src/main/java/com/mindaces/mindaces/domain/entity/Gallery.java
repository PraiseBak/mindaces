package com.mindaces.mindaces.domain.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Getter
@Entity
@DynamicInsert
@Builder
@Table(name = "GALLERY")
public class Gallery
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gallery_idx")
    private Long galleryIdx;

    @Column(name = "gallery_url",nullable = false)
    private String galleryURL;

    @Column(name = "gallery_name")
    private String galleryName;

    @Column(name = "special_gallery",columnDefinition = "tinyint(1) default 0")
    private Boolean specialGallery;

    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime galleryCreatedDate;

    @Column(name = "recommend_standard",columnDefinition = "bigint default 10")
    private Long recommendStandard;

    @Column(name = "recommend_likes_sum",columnDefinition = "bigint default 0")
    private Long recommendedLikesSum;


    @Column(name = "recommend_board_count",columnDefinition = "bigint default 0")
    private Long recommendedBoardCount;



    public void updateRecommendBoardCount()
    {
        this.recommendedBoardCount += 1L;
    }

    public void addRecommendedLikesSum(Long likes)
    {
        recommendedLikesSum += likes;
    }

    public void updateRecommendStandard(Long newRecommendedStandard)
    {
        this.recommendStandard = newRecommendedStandard;
    }
}
