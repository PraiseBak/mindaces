package com.mindaces.mindaces.dto;

import com.mindaces.mindaces.domain.entity.Like;
import lombok.Builder;

import javax.persistence.Column;

//TODO LIKE 추가해야함
public class LikeDto
{
    String likeIP;
    String disLikeIP;
    Long like;
    Long disLike;

    public Like toEntity()
    {
        Like like = Like.builder()
                .likeIP(likeIP)
                .disLikeIP(disLikeIP)
                .like(like)
                .disLike(disLike)
                .build();

        return like;
    }

    @Builder
    public LikeDto(String likeIP,String disLikeIP,Long like, Long disLike)
    {
        this.likeIP = likeIP;
        this.disLikeIP = disLikeIP;
        this.like = like;
        this.disLike = disLike;

    }



}
