package com.mindaces.mindaces.domain.repository;

import com.mindaces.mindaces.domain.entity.Likes;
import com.mindaces.mindaces.domain.entity.LikesID;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikesRepository extends JpaRepository<Likes, LikesID>
{
    Likes findByContentIdxAndIsComment(Long contentIdx,boolean isComment);
    void deleteByIsCommentAndContentIdx(boolean isComment, Long commentIdx);

}
