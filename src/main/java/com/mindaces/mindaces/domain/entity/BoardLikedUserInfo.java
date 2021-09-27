package com.mindaces.mindaces.domain.entity;


import lombok.*;

import javax.persistence.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "BOARD_LIKED_USERINFO")
public class BoardLikedUserInfo
{
    @Id
    @Column(name="board_like_idx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long boardLikeIdx;

    @Column(name="content_idx")
    public Long contentIdx;

    @Column(nullable = false)
    public String gallery;

    @Column(name="liked_ip",columnDefinition="varchar(45) default '-'")
    public String likedIP;

    @Column(name="disliked_ip",columnDefinition="varchar(45) default '-'")
    public String disLikedIP;

    @Column(name="user_name",columnDefinition="varchar(45) default '-'")
    public String userName;

    @Builder
    public BoardLikedUserInfo(String gallery, Long contentIdx, String likedIP, String disLikedIP, String userName)
    {
        this.gallery = gallery;
        this.contentIdx = contentIdx;
        this.likedIP = likedIP;
        this.disLikedIP = disLikedIP;
        this.userName = userName;
    }

}
