package com.mindaces.mindaces.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikesID implements Serializable
{
    @Column(name = "content_idx",nullable = false)
    private Long contentIdx;

    @Column(name = "is_comment")
    private boolean isComment;
}
