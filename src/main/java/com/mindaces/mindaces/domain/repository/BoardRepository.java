package com.mindaces.mindaces.domain.repository;

import com.mindaces.mindaces.domain.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long>
{
    Page<Board> findAll(Pageable pageable);
    List<Board> findByGalleryContaining(String keyword);
}