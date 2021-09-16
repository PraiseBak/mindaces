package com.mindaces.mindaces.domain.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@NoArgsConstructor
@DynamicInsert
@Table(name="LIKES")
@IdClass(LikesID.class)
public class Likes implements Serializable
{
    @Column(name = "is_comment")
    @Id
    private boolean isComment;

    //board,comment 둘의 개추들 모두 담음
    @Id
    @Column(name = "content_idx",nullable = false)
    private Long contentIdx;

    @Column(columnDefinition = "bigint default 0")
    private Long likes;

    @Column(columnDefinition = "bigint default 0")
    private Long dislikes;


    public void updateLikes()
    {
        this.likes += 1L;
    }

    public void updateDislikes()
    {
        this.dislikes += 1L;
    }

    public void setContentIdx(Long contentIdx)
    {
        this.contentIdx = contentIdx;
    }

    @Builder
    public Likes(Long contentIdx,Long likes,Long dislikes,boolean isComment)
    {
        this.contentIdx = contentIdx;
        this.likes = likes;
        this.dislikes = dislikes;
        this.isComment = isComment;
    }

}
