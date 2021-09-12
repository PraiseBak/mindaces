package com.mindaces.mindaces.dto;

import com.mindaces.mindaces.domain.entity.Comment;
import lombok.*;

import java.util.Comparator;


@Getter
@Setter
@ToString
@NoArgsConstructor
public class CommentDto
{
    Long commentIdx;
    Long boardIdx;
    String gallery;
    String user;
    String content;
    String commentPassword;
    Long isLogged;
    Long likes;
    String createdDate;
    String modifiedDate;

    public Comment toEntity()
    {
        Comment comment = Comment.builder()
               .commentIdx(commentIdx)
               .boardIdx(boardIdx)
               .gallery(gallery)
               .user(user)
               .content(content)
               .commentPassword(commentPassword)
               .isLogged(isLogged)
               .likes(likes)
               .build();
        return comment;
    }

    @Builder
    public CommentDto(Long commentIdx,Long boardIdx,String gallery,String user,String content,String commentPassword,Long isLogged,Long likes,String createdDate,String modifiedDate)
    {
        this.commentIdx = commentIdx;
        this.boardIdx = boardIdx;
        this.gallery = gallery;
        this.user = user;
        this.content = content;
        this.commentPassword = commentPassword;
        this.isLogged = isLogged;
        this.likes = likes;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }
}

