package com.mindaces.mindaces.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
//LocalDate 등 저장하기 위해 사용하는 클래스
//누가 수정하고 삭제하는 등을 했는지 알기 위해 추가함
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "USER_OBJ")
public class UserObj extends BaseTimeEntity
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "obj_idx")
    private Long objIdx;

    @Column(name = "user_idx")
    private Long userIdx;

    @Column(name = "obj_title",nullable = false, length = 45)
    private String objTitle;

    @Column(name = "obj_content",nullable = false)
    private String objContent;

    @Column(name = "obj_day",columnDefinition = "INT UNSIGNED")
    private Long objDay;

    @Column(name = "is_represent_obj",columnDefinition = "tinyint(1) default 0")
    private Long isRepresentObj;

    @Builder
    UserObj(String objTitle,String objContent,Long objDay,Long isRepresentObj)
    {
        this.objContent = objContent;
        this.objTitle = objTitle;
        this.objDay = objDay;
        this.isRepresentObj = isRepresentObj;
    }

    public void setUserIdx(Long userIdx)
    {
        this.userIdx = userIdx;
    }
}
