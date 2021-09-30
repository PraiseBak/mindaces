package com.mindaces.mindaces.domain.entity;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.io.Serializable;

@Entity
@IdClass(BoardInfoID.class)
@NoArgsConstructor
@Getter
@DynamicInsert
public class BoardInfo implements Serializable
{
    @Id
    @Column(name = "content_idx",nullable = false)
    private Long contentIdx;

    @Id
    @Column(nullable = false)
    private String gallery;

    @Column(name = "visited_num",columnDefinition = "bigint default 1")
    private Long visitedNum;

    @Column(name = "is_recommended_board",columnDefinition = "tinyint(1) default 0")
    private Boolean isRecommendedBoard;

    @Column(name = "comment_count",columnDefinition = "bigint default 0")
    private Long commentCount;

    public void updateContentIdx(Long contentIdx)
    {
        this.contentIdx = contentIdx;
    }

    public void updateGallery(String gallery)
    {
        this.gallery = gallery;
    }

    public void updateIsRecommendedBoard()
    {
        this.isRecommendedBoard = true;
    }

    public void updateVisitedNum()
    {
        this.visitedNum += 1;
    }

    public void updateCommentCount(Long commentCount)
    {
        this.commentCount = commentCount;
    }
}
