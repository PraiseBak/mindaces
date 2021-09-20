package com.mindaces.mindaces.domain.entity;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@NoArgsConstructor
@DynamicInsert
@Table(name= "likes")
@IdClass(LikesID.class)
public class Likes implements Serializable
{
    @Id
    @Column(name = "is_comment")
    private boolean isComment;

    //board,comment 둘의 개추들 모두 담음
    @Id
    @Column(name = "content_idx",nullable = false)
    private Long contentIdx;

    @Column(columnDefinition = "bigint default 0",name = "content_like")
    private Long like;

    @Column(columnDefinition = "bigint default 0",name = "content_dislike")
    private Long dislike;

    public void updateLike()
    {
        this.like += 1L;
    }

    public void updateDislike()
    {
        this.dislike += 1L;
    }

    public void setContentIdx(Long contentIdx)
    {
        this.contentIdx = contentIdx;
    }

    @Builder
    public Likes(Long contentIdx,Long like,Long dislike,boolean isComment)
    {
        this.contentIdx = contentIdx;
        this.like = like;
        this.dislike = dislike;
        this.isComment = isComment;
    }

}
