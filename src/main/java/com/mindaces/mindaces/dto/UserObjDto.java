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
    private Long objIdx;
    private String objTitle;
    private String objContent;
    private Long objDay;
    private Boolean isRepresentObj;

    public UserObj toEntity()
    {
        UserObj userObj = UserObj.builder()
                .objDay(this.objDay)
                .isRepresentObj(this.isRepresentObj)
                .objContent(this.objContent)
                .objTitle(this.objTitle)
                .build();
        return userObj;
    }


}
