package com.mindaces.mindaces.domain.entity;


import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "liked_user_info")
public class LikedUserInfo
{
    @Id
    @Column(name="like_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long likeIdx;
    @Column(nullable = false)
    public String gallery;
    @Column(name="board_idx",nullable = false)
    public Long boardIdx;
    @Column(name="liked_ip",columnDefinition="varchar(45) default '-'")
    public String likedIP;
    @Column(name="disliked_ip",columnDefinition="varchar(45) default '-'")
    public String disLikedIP;
    @Column(name="user_name",columnDefinition="varchar(45) default '-'")
    private String userName;

    @Builder
    public LikedUserInfo(String gallery,Long boardIdx,String likedIP,String disLikedIP,String userName)
    {
        this.gallery = gallery;
        this.boardIdx = boardIdx;
        this.likedIP = likedIP;
        this.disLikedIP = disLikedIP;
        this.userName = userName;
    }



}
