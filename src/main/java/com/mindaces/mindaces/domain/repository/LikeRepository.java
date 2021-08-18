package com.mindaces.mindaces.domain.repository;

import com.mindaces.mindaces.domain.entity.Gallery;
import com.mindaces.mindaces.domain.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long>
{



}
