package com.mindaces.mindaces.domain.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;


//아래의 칼럼들을 상속한 클래스에서 칼럼으로 인식하도록 하는 어노테이션
@MappedSuperclass
//현재 클래스에 Auditing 기능 추가
@EntityListeners(AuditingEntityListener.class)
@Getter
public abstract class BaseTimeEntity
{
    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;
}
