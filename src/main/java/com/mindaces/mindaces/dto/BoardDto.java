package com.mindaces.mindaces.dto;

import com.mindaces.mindaces.domain.entity.Board;
import com.mindaces.mindaces.domain.entity.Gallery;
import com.mindaces.mindaces.domain.entity.User;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BoardDto
{

    private Gallery gallery;
    private User user;
    private Long contentIdx;
    private String title;
    private String content;

    public Board toEntity()
    {
        Board board = Board.builder()
                .gallery(gallery)
                .user(user)
                .contentIdx(contentIdx)
                .title(title)
                .content(content)
                .build();
        return board;
    }

    @Builder
    public BoardDto(Gallery gallery,User user,Long contentIdx,String title,String content)
    {
        this.gallery = gallery;
        this.user = user;
        this.contentIdx = contentIdx;
        this.title = title;
        this.content = content;
    }


}