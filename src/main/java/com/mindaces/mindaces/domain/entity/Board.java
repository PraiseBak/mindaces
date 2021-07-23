package com.mindaces.mindaces.domain.entity;

import lombok.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "BOARD")
public class Board extends BaseTimeEntity
{

    @OneToOne
    @JoinColumn(name = "gallery_idx")
    private Gallery gallery;

    @OneToOne
    @JoinColumn(name = "user_idx")
    private User user;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "content_idx")
    private Long contentIdx;


    @Column(nullable = false,length = 45)
    private String title;

    @Column(nullable = false)
    private String content;


    @Builder
    public Board(Gallery gallery,User user,Long contentIdx,String title,String content)
    {
        this.gallery = gallery;
        this.user = user;
        this.contentIdx = contentIdx;
        this.title = title;
        this.content = content;
    }


}
