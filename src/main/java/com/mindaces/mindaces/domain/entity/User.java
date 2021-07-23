package com.mindaces.mindaces.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="USER")
public class User
{
    @Id
    //auto increment 의미
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_idx",nullable = false)
    private Long userIdx;

    @Column(name="user_id",length = 45,nullable = false)
    private String userID;

    @Column(name="user_pw",length = 45,nullable = false)
    private String userPassword;

    @Column(name="user_email",length = 45 ,nullable = false)
    private String userEmail;

    @CreatedDate
    @Column(name = "user_create_time",nullable = false)
    private LocalDateTime createdDate;

    @Builder
    public User(Long userIdx,String userId,String userPassword,String userEmail)
    {
        userIdx = userIdx;
        userId = userId;
        userPassword = userPassword;
        userEmail = userEmail;
    }

}
