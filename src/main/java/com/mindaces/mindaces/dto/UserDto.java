package com.mindaces.mindaces.dto;

import com.mindaces.mindaces.domain.entity.User;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserDto
{

    private Long userIdx;
    private String userID;
    private String userPassword;
    private String userEmail;


    public User toEntity()
    {
        User user = User.builder()
                .userID(userID)
                .userPassword(userPassword)
                .userEmail(userEmail)
                .build();
        return user;
    }

    @Builder
    public UserDto(String userID,String userPassword,String userEmail)
    {
        this.userID = userID;
        this.userPassword = userPassword;
        this.userEmail = userEmail;
    }

}
