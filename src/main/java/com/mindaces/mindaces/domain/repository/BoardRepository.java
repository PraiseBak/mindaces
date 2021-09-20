package com.mindaces.mindaces.domain.repository;

import com.mindaces.mindaces.domain.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long>
{
    Page<Board> findByGallery(String keyword, Pageable pageable);
    <T> T findByGalleryAndContentIdx(String gallery,Long contentIdx,Class<T> type);
    Long countBoardByGallery(String gallery);
    //참고 List<Board> findByContentIdxIn(List<Long> ids);
    List<Board> findTop10ByOrderByLikesLikeDesc();
    Page<Board> findByGalleryAndLikesLikeGreaterThan(String gallery,Pageable pageable,Long likesLike);
    Page<Board> findByGalleryAndIsRecommendedBoardIsTrue(String gallery,Pageable pageable);

}

