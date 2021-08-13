package com.mindaces.mindaces.domain.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "content_idx")
    private Long contentIdx;
    @Column(name="gallery_idx")
    private Long galleryIdx;
    @Column(name="user_idx")
    private Long userIdx;
    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    private String title;
    @CreatedDate
    @Column(name="created_time")
    private LocalDateTime createdTime;
    @LastModifiedDate
    @Column(name="modified_time" )
    private LocalDateTime modifiedTime;

    @Builder
    public Board(Long contentIdx,Long galleryIdx,Long userIdx,String content,String title,LocalDateTime createdTime,LocalDateTime modifiedTime)
    {
        this.contentIdx = contentIdx;
        this.galleryIdx = galleryIdx;
        this.userIdx = userIdx;
        this.content = content;
        this.title = title;
        this.createdTime = createdTime;
        this.modifiedTime = modifiedTime;
    }



}
