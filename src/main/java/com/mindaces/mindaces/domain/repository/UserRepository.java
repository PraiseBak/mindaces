package com.mindaces.mindaces.domain.repository;

import com.mindaces.mindaces.domain.entity.User;
import org.hibernate.annotations.SQLUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>
{
    Optional<User> findByUserEmail(String userEmail);
    Optional<User> findByUserID(String userID);
}
