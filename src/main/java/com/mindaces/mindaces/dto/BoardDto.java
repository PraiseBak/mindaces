package com.mindaces.mindaces.dto;

import com.mindaces.mindaces.domain.entity.Board;
import com.mindaces.mindaces.domain.entity.BoardInfo;
import com.mindaces.mindaces.domain.entity.Likes;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BoardDto
{
    private Long contentIdx;
    private String gallery;
    private String user;
    private String title;
    private String content;
    private Likes likes;
    private String password;
    private Long isLoggedUser;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private BoardInfo boardInfo;


    public Board toEntity()
    {
        Board board = Board.builder()
                .gallery(gallery)
                .user(user)
                .contentIdx(contentIdx)
                .title(title)
                .content(content)
                .likes(likes)
                .password(password)
                .isLoggedUser(isLoggedUser)
                .boardInfo(boardInfo)
                .build();
        return board;
    }


    @Builder
    public BoardDto(String gallery, String user, Long contentIdx, String title, String content,Likes likes,String password,Long isLoggedUser,LocalDateTime createdDate,LocalDateTime modifiedDate,BoardInfo boardInfo)
    {
        this.gallery = gallery;
        this.user = user;
        this.contentIdx = contentIdx;
        this.title = title;
        this.content = content;
        this.likes = likes;
        this.password = password;
        this.isLoggedUser = isLoggedUser;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.boardInfo = boardInfo;
    }

}
