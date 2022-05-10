package com.mindaces.mindaces.domain.entity;

import javax.persistence.*;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name="UNCHECKED_USER")
public class UncheckedUser
{
    @Id
    //auto increment 의미
    @Column(name="user_key")
    private String key;

    @Column(name="user_id",length = 45,nullable = false)
    private String userID;
}
