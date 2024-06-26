package com.mindaces.mindaces.domain.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "BOARD")
public class Board extends BaseTimeEntity
{

    @Column(nullable = false, length = 45)
    private String gallery;

    @Column(nullable = false)
    private String user;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "content_idx")
    private Long contentIdx;

    @Column(nullable = false, length = 45)
    private String title;

    @Column(columnDefinition = "text", nullable = false)
    private String content;

    @OneToOne(cascade = CascadeType.ALL)
    private Likes likes;

    @Column(length = 80, nullable = false)
    private String password;

    @Column(columnDefinition = "tinyint(1) default 0", name = "is_logged_user")
    private Long isLoggedUser;


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "content_idx")
    private List<BoardLikedUserInfo> boardLikedUserInfoList = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    private BoardInfo boardInfo;

    public void updateLikes(Likes likes)
    {
        this.likes = likes;
    }

    public void updateContent(String content)
    {
        this.content = content;
    }

    public void updateTitle(String title)
    {
        this.title = title;
    }

    public void updatePassword(String password)
    {
        this.password = password;
    }

    public void updateUser(String user)
    {
        this.user = user;
    }

    public void updateBoardInfo(BoardInfo boardInfo)
    {
        this.boardInfo = boardInfo;
    }



    @Builder
    public Board(String gallery, String user, Long contentIdx, String title, String content, Likes likes, String password, Long isLoggedUser)
    {
        this.gallery = gallery;
        this.user = user;
        this.contentIdx = contentIdx;
        this.title = title;
        this.content = content;
        this.likes = likes;
        this.password = password;
        this.isLoggedUser = isLoggedUser;
    }
}
