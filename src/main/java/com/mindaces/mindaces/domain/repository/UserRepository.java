package com.mindaces.mindaces.domain.repository;

import com.mindaces.mindaces.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>
{
    //Optional<User> findByUserEmail(String userEmail);
    Optional<User> findByUserID(String userID);
}
