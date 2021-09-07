package com.mindaces.mindaces.dto;

import com.mindaces.mindaces.domain.entity.Comment;
import lombok.*;


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
    Long dislikes;

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
               .dislikes(dislikes)
               .build();
        return comment;
    }

    @Builder
    public CommentDto(Long commentIdx,Long boardIdx,String gallery,String user,String content,String commentPassword,Long isLogged,Long likes,Long dislikes)
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