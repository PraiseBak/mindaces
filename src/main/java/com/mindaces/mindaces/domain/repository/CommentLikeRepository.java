package com.mindaces.mindaces.domain.repository;

import com.mindaces.mindaces.domain.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike,Long>
{
    CommentLike findByCommentIdxAndLikedIP(Long commentIdx,String likedIP);
    CommentLike findByCommentIdxAndUserName(Long commentIdx, String userName);
}
