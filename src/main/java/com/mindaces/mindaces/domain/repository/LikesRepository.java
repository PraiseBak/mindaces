package com.mindaces.mindaces.domain.repository;

import com.mindaces.mindaces.domain.entity.Likes;
import com.mindaces.mindaces.domain.entity.LikesID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes, LikesID>
{
    Likes findByContentIdxAndIsComment(Long boardIdx,boolean isComment);
    void deleteByIsCommentAndContentIdx(boolean isComment, Long commentIdx);
}
