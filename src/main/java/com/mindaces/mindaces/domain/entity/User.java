package com.mindaces.mindaces.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="USER")
@EntityListeners(AuditingEntityListener.class)
public class User
{
    @Id
    //auto increment 의미
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_idx")
    private Long userIdx;

    @Column(name="user_id",length = 45,nullable = false)
    private String userID;

    @Column(name="user_pw",length = 80,nullable = false)
    private String userPassword;

    @Column(name="user_email",length = 45 ,nullable = false)
    private String userEmail;

    @CreatedDate
    @Column(name = "user_create_time", nullable = false, updatable = false)
    private LocalDateTime createdDate;


    @Builder
    public User(Long userIdx,String userID,String userPassword,String userEmail)
    {
        this.userIdx = userIdx;
        this.userID = userID;
        this.userPassword = userPassword;
        this.userEmail = userEmail;
    }

}
