package com.mindaces.mindaces.dto;

import com.mindaces.mindaces.domain.entity.UserObj;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserObjDto
{
    private String objTitle;
    private String objContent;
    private Integer objDay;

    public UserObj toEntity()
    {
        UserObj userObj = UserObj.builder()
                .objDay(this.objDay)
                .objContent(this.objContent)
                .objTitle(this.objTitle)
                .build();
        return userObj;
    }


}
