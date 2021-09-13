package com.mindaces.mindaces.domain.repository;

import com.mindaces.mindaces.domain.entity.Comment;
import com.mindaces.mindaces.dto.CommentDto;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long>
{
    List<Comment> getCommentByBoardIdxAndGalleryOrderByCreatedDateAsc(Long boardIdx, String gallery);
    List<Comment> getCommentByBoardIdxAndGallery(Long boardIdx, String gallery);



}
