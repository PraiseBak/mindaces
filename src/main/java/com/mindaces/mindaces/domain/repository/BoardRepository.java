package com.mindaces.mindaces.domain.repository;

import com.mindaces.mindaces.domain.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board,Long>
{

}
