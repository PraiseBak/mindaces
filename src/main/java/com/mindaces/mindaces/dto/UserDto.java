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
    private String userId;
    private String userPassword;
    private String userEmail;

    public User toEntity()
    {
        User user = User.builder()
                .userIdx(userIdx)
                .userId(userId)
                .userPassword(userPassword)
                .userEmail(userEmail)
                .build();
        return user;
    }

    @Builder
    public UserDto(Long userIdx,String userId,String userPassword,String userEmail)
    {
        userIdx = userIdx;
        userId = userId;
        userPassword = userPassword;
        userEmail = userEmail;
    }

}
