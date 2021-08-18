package com.mindaces.mindaces.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.io.Serializable;


@Getter
@Entity
@Setter
@NoArgsConstructor
@Table(name = "LIKES")
public class Like implements Serializable
{

    @Id
    @Column(name="content_idx")
    private Long contentIdx;

    @Column(name="like_ip")
    private String likeIP;

    @Column(name="notlike_ip")
    private String disLikeIP;


    @Builder
    public Like(Long contentIdx, String likeIP, String disLikeIP)
    {
        this.contentIdx = contentIdx;
        this.likeIP = likeIP;
        this.disLikeIP = disLikeIP;
    }

}
