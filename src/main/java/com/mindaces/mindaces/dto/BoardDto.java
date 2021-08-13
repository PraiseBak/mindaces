package com.mindaces.mindaces.dto;


import com.mindaces.mindaces.domain.entity.Board;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
//파라미터가 없는 생성자를 자동으로 만들어줌
@AllArgsConstructor
public class BoardDto
{
    private Long contentIdx;
    private Long galleryIdx;
    private Long userIdx;
    private String content;
    private String title;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;


    public Board toEntity()
    {
        Board build = Board.builder()
                .contentIdx(contentIdx)
                .galleryIdx(galleryIdx)
                .userIdx(userIdx)
                .content(content)
                .title(title)
                .createdTime(createdTime)
                .modifiedTime(modifiedTime)
                .build();
        return build;
    }


}