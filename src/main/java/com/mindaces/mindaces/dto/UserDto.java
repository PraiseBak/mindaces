package com.mindaces.mindaces.dto;

import com.mindaces.mindaces.domain.entity.User;
import lombok.*;

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
