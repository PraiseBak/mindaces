package com.mindaces.mindaces.domain.repository;

import com.mindaces.mindaces.domain.entity.Comment;
import com.mindaces.mindaces.dto.CommentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long>
{
    List<Comment> getCommentByBoardIdxAndGalleryOrderByCreatedDateAsc(Long boardIdx, String gallery);
    Long countByBoardIdx(Long boardIdx);
    Page<Comment> findByGalleryAndBoardIdx(String galleryName, Long boardIdx, Pageable pageable);
    Page<Comment> findByUserAndIsLogged(String user, Long isLogged, Pageable pageable);
    Long countByGalleryAndBoardIdx(String galleryName, Long boardIdx);
    void deleteByBoardIdxAndGallery(Long contentIdx,String gallery);
    Long countByUserAndIsLogged(String user, Long isLogged);
}
