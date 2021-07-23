package com.mindaces.mindaces.domain.repository;

import com.mindaces.mindaces.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>
{
}
