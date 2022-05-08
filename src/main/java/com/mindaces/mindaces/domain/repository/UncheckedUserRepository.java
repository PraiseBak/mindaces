package com.mindaces.mindaces.domain.repository;

import com.mindaces.mindaces.domain.entity.UncheckedUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UncheckedUserRepository extends JpaRepository<UncheckedUser,Long>
{
    UncheckedUser getByKey(String key);
    void deleteByKey(String key);
}
