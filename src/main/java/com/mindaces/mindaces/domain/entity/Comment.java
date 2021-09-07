package com.mindaces.mindaces.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Setter
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@DynamicInsert
@Table(name = "COMMENT")
@NoArgsConstructor
public class Comment
{
    @Id
    @Column(name = "comment_idx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long commentIdx;

    @Column(name = "board_idx",nullable = false)
    Long boardIdx;

    @Column(nullable = false)
    String gallery;

    @Column(nullable = false)
    String user;

    @Column(nullable = false)
    String content;

    @Column(name = "comment_password",nullable = false,length = 80)
    String commentPassword;

    @Column(columnDefinition = "tinyint(1) default 0",name = "is_logged_user")
    Long isLogged;

    @Column(columnDefinition = "bigint default 0")
    private Long likes;

    @Column(name="dis_likes",columnDefinition = "bigint default 0")
    private Long dislikes;


    @Builder
    public Comment(Long commentIdx,Long boardIdx,String gallery,String user,String content,String commentPassword,Long isLogged,Long likes,Long dislikes)
    {
        this.commentIdx = commentIdx;
        this.boardIdx = boardIdx;
        this.gallery = gallery;
        this.user = user;
        this.content = content;
        this.commentPassword = commentPassword;
        this.isLogged = isLogged;
        this.likes = likes;
        this.dislikes = dislikes;
    }

}
