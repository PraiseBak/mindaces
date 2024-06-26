package com.mindaces.mindaces.domain.entity;


import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;


@Setter
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@DynamicInsert
@Table(name = "COMMENT_LIKED_USER_INFO")
@NoArgsConstructor
public class CommentLikedUserInfo
{
    @Id
    @Column(name = "like_idx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long likeIdx;

    @Column(name="comment_idx")
    public Long commentIdx;

    @Column(name="liked_ip",columnDefinition="varchar(45) default '-'")
    public String likedIP;
    @Column(name="user_name",columnDefinition="varchar(45) default '-'")
    public String username;

    @Builder
    public CommentLikedUserInfo(Long commentIdx, String likedIP, String username)
    {
        this.commentIdx = commentIdx;
        this.likedIP = likedIP;
        this.username = username;
    }



}
