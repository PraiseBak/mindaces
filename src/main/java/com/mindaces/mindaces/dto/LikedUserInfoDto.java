package com.mindaces.mindaces.dto;

import com.mindaces.mindaces.domain.entity.Gallery;
import com.mindaces.mindaces.domain.entity.LikedUserInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;


@Getter
@Setter
@ToString
@NoArgsConstructor

public class LikedUserInfoDto
{
    public String gallery;
    public Long boardIdx;
    public String likedIP;
    public String disLikedIP;
    private String userName;


    public LikedUserInfo toEntity()
    {

        LikedUserInfo linkedUserInfo = LikedUserInfo.builder()
                .gallery(gallery)
                .boardIdx(boardIdx)
                .likedIP(likedIP)
                .disLikedIP(disLikedIP)
                .userName(userName)
                .build();

        return LikedUserInfo.builder().build();
    }



}
