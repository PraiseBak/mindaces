package com.mindaces.mindaces.dto;

import com.mindaces.mindaces.domain.entity.User;
import com.mindaces.mindaces.helper.constants.SocialLoginType;
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
    //use as a nickname
    private String userID;
    private String userPassword;
    private String userEmail;
    @Enumerated(EnumType.STRING)
    private SocialLoginType provider;





    public User toEntity()
    {
        User user = User.builder()
                .userIdx(userIdx)
                .userID(userID)
                .userPassword(userPassword)
                .userEmail(userEmail)
                .build();
        return user;
    }

    @Builder
    public UserDto(Long userIdx,String userID,String userPassword,String userEmail)
    {
        this.userIdx = userIdx;
        this.userID = userID;
        this.userPassword = userPassword;
        this.userEmail = userEmail;
    }

}
