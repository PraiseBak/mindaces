package com.mindaces.mindaces.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardInfoID implements Serializable
{

    @Column(name = "content_idx",nullable = false)
    private Long contentIdx;

    @Column(nullable = false)
    private String gallery;
}

