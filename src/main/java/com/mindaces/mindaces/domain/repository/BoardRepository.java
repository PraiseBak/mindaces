package com.mindaces.mindaces.domain.repository;

import com.mindaces.mindaces.domain.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long>
{
    Page<Board> findByGallery(String keyword, Pageable pageable);
    <T> T findByGalleryAndContentIdx(String gallery,Long contentIdx,Class<T> type);
    Long countBoardByGallery(String gallery);
    //참고 파라미터의 ids에 해당하는 것만 find
    //List<Board> findByContentIdxIn(List<Long> ids);
    List<Board> findTop10ByOrderByLikesLikeDesc();
    Page<Board> findByGalleryAndBoardInfoIsRecommendedBoardIsTrue(String gallery, Pageable pageable);
    Long countBoardByUserAndIsLoggedUser(String user,Long isLoggedUser);
    Page<Board> findByUserAndBoardInfoIsRecommendedBoardIsTrue(String user, Pageable pageable);
    Page<Board> findByUser(String user, Pageable pageable);
    Long countBoardByUserAndBoardInfoIsRecommendedBoardIsTrueAndIsLoggedUser(String user,Long isLoggedUser);
    Page<Board> findByTitleContainingAndBoardInfoIsRecommendedBoard(String title, Boolean isRecommendedBoard, Pageable pageable);
    Page<Board> findByContentContainingAndBoardInfoIsRecommendedBoard(String conetnt, Boolean isRecommendedBoard, Pageable pageable);
    Long countBoardByTitleContaining(String title);
    Long countBoardByContentContaining(String content);

}

