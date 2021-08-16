package com.mindaces.mindaces.dto;

import com.mindaces.mindaces.domain.entity.Board;
import com.mindaces.mindaces.domain.entity.Gallery;
import com.mindaces.mindaces.domain.entity.User;
import lombok.*;
import org.apache.tomcat.jni.Local;

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
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;


    public Board toEntity()
    {
        Board board = Board.builder()
                .gallery(gallery)
                .user(user)
                .contentIdx(contentIdx)
                .title(title)
                .content(content)
                .createdDate(createdDate)
                .modifiedDate(modifiedDate)
                .build();
        return board;
    }



    @Builder
    public BoardDto(String gallery, String user, Long contentIdx, String title, String content, LocalDateTime createdDate, LocalDateTime modifiedDate)
    {
        this.gallery = gallery;
        this.user = user;
        this.contentIdx = contentIdx;
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;

    }

}
