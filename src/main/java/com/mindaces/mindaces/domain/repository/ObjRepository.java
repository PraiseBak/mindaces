package com.mindaces.mindaces.domain.repository;

import com.mindaces.mindaces.domain.entity.UserObj;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ObjRepository extends JpaRepository<UserObj,Long>
{

}
