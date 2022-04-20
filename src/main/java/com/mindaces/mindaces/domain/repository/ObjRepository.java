package com.mindaces.mindaces.domain.repository;

import com.mindaces.mindaces.domain.entity.UserObj;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ObjRepository extends JpaRepository<UserObj,Long>
{
    List<UserObj> findAllByUserIdx(Long userID);
    List<UserObj> findAllByIsRepresentObjAndUserIdx(Boolean isRepresentObj,Long userIdx);
}
