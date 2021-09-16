package com.mindaces.mindaces.domain.repository;

import com.mindaces.mindaces.domain.entity.CommentLikeUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLikeUserInfo,Long>
{
    CommentLikeUserInfo findByCommentIdxAndLikedIP(Long commentIdx, String likedIP);
    CommentLikeUserInfo findByCommentIdxAndUserName(Long commentIdx, String userName);
}
