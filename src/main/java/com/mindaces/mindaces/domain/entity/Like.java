package com.mindaces.mindaces.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.bytebuddy.implementation.bind.annotation.Default;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.context.annotation.Primary;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;



@Getter
@Entity
@NoArgsConstructor
@Table(name = "Like")
public class Like
{
    @Id
    @Column(name="board_idx")
    Long boardIdx;

    @Column(name="like_ip")
    String likeIP;

    @Column(name="notlike_ip")
    String disLikeIP;

    @Column(columnDefinition = "integer default 0")
    Long like;

    @Column(name="not_like")
    @ColumnDefault("0")
    Long disLike;

    @Builder
    public Like(String likeIP,String disLikeIP,Long like,Long disLike)
    {
        this.likeIP = likeIP;
        this.disLikeIP = disLikeIP;
        this.like = like;
        this.disLike = disLike;
    }

}
