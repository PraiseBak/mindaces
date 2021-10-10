package com.mindaces.mindaces.domain.repository;

import com.mindaces.mindaces.domain.entity.CommentLikedUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLikedUserInfo,Long>
{
    CommentLikedUserInfo findByCommentIdxAndLikedIP(Long commentIdx, String likedIP);
    CommentLikedUserInfo findByCommentIdxAndUsername(Long commentIdx, String username);
}
