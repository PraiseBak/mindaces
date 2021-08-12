package com.mindaces.mindaces.domain.entity;

import lombok.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "BOARD")
public class Board extends BaseTimeEntity
{

    @Column(nullable = false,length = 45)
    private String gallery;

    @Column(nullable = false,length = 45)
    private String user;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "content_idx")
    private Long contentIdx;


    @Column(nullable = false,length = 45)
    private String title;

    @Column(nullable = false)
    private String content;




    @Builder
    public Board(String gallery,String user,Long contentIdx,String title,String content,LocalDateTime createdDate,LocalDateTime modifiedDate)
    {
        this.gallery = gallery;
        this.user = user;
        this.contentIdx = contentIdx;
        this.title = title;
        this.content = content;
    }


}
