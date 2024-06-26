package com.mindaces.mindaces.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@DynamicInsert
@Table(name = "COMMENT")
@NoArgsConstructor
public class Comment extends BaseTimeEntity
{
    @Id
    @Column(name = "comment_idx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long contentIdx;

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

    @OneToOne(cascade = CascadeType.ALL)
    private Likes likes;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "comment_idx")
    private List<CommentLikedUserInfo> commentLikedUserInfoList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_comment_idx")
    private List<Comment> nestedCommentList = new ArrayList<>();

    @Column(name = "parent_comment_idx")
    private Long parentCommentIdx;

    @Builder
    public Comment(Long contentIdx, Long boardIdx, String gallery, String user, String content, String commentPassword, Long isLogged, Likes likes,Long parentCommentIdx)
    {
        this.contentIdx = contentIdx;
        this.boardIdx = boardIdx;
        this.gallery = gallery;
        this.user = user;
        this.content = content;
        this.commentPassword = commentPassword;
        this.isLogged = isLogged;
        this.likes = likes;
        this.parentCommentIdx = parentCommentIdx;
    }

    public void setLikes(Likes likes)
    {
        this.likes = likes;
    }

    public void addNestedComment(Comment nestedComment)
    {
        this.nestedCommentList.add(nestedComment);
    }


}



