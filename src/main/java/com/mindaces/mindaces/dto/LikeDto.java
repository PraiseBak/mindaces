package com.mindaces.mindaces.dto;

import com.mindaces.mindaces.domain.entity.Board;
import com.mindaces.mindaces.domain.entity.Like;
import lombok.*;

//TODO LIKE 추가해야함
@Getter
@Setter
@ToString
@NoArgsConstructor
public class LikeDto
{
    private Long contentIdx;
    private String likeIP;
    private String disLikeIP;

    public Like toEntity()
    {
        Like like = Like.builder()
                .contentIdx(contentIdx)
                .likeIP(likeIP)
                .disLikeIP(disLikeIP)
                .build();
        return like;
    }

    @Builder
    public LikeDto(Long contentIdx,String likeIP,String disLikeIP)
    {
        this.contentIdx = contentIdx;
        this.likeIP = likeIP;
        this.disLikeIP = disLikeIP;
    }


}
