package com.mindaces.mindaces.domain.entity;

//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import org.aspectj.weaver.GeneratedReferenceTypeDelegate;
//import org.springframework.data.jpa.domain.support.AuditingEntityListener;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//
//import javax.persistence.*;
//import java.util.List;
//
////유저
//@Entity
//@Getter
//@NoArgsConstructor
//@Table(name = "USER_OBJ_INFO")
//public class UserObjInfo
//{
//
//    //목표를 작성한 유저 id
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name="user_idx")
//    private Long userIdx;
//
//    //유저가 작성한 목표 리스트
//    //엔티티간의 영향을 주는 범위 설정
//    @OneToMany(cascade = CascadeType.ALL)
//    @JoinColumn(name = "user_idx")
//    private List<UserObj> userObjList;
//
//}
