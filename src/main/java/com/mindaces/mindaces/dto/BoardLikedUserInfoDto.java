package com.mindaces.mindaces.dto;

import com.mindaces.mindaces.domain.entity.BoardLikedUserInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@NoArgsConstructor

public class BoardLikedUserInfoDto
{
    public String gallery;
    public Long contentIdx;
    public String likedIP;
    public String disLikedIP;
    private String userName;


    public BoardLikedUserInfo toEntity()
    {

        BoardLikedUserInfo linkedUserInfo = BoardLikedUserInfo.builder()
                .gallery(gallery)
                .contentIdx(contentIdx)
                .likedIP(likedIP)
                .disLikedIP(disLikedIP)
                .userName(userName)
                .build();

        return BoardLikedUserInfo.builder().build();
    }



}
