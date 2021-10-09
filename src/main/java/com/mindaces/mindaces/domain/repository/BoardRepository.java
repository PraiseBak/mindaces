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

    //title,gallery를 포함하는 [전체] 게시물들
    Page<Board> findByTitleContainingAndGallery(String title, Pageable pageable,String gallery);
    //title,gallery를 포함하는 [개념글] 게시물들
    Page<Board> findByTitleContainingAndGalleryAndBoardInfoIsRecommendedBoardIsTrue(String title,Pageable pageable,String gallery);
    //title를 포함하는 [전체] 게시물들
    Page<Board> findByTitleContaining(String title, Pageable pageable);
    //title를 포함하는 [개념글] 게시물들
    Page<Board> findByTitleContainingAndBoardInfoIsRecommendedBoardIsTrue(String title,Pageable pageable);
    //content,gallery를 포함하는 [전체] 게시물들
    Page<Board> findByContentContainingAndGallery(String content, Pageable pageable,String gallery);
    //content,gallery를 포함하는 내용 [개념글] 게시물들
    Page<Board> findByContentContainingAndGalleryAndBoardInfoIsRecommendedBoardIsTrue(String content, Pageable pageable,String gallery);
    //content를 포함하는 [전체] 게시물들
    Page<Board> findByContentContaining(String content, Pageable pageable);
    //content를 포함하는 [개념글] 게시물들
    Page<Board> findByContentContainingAndBoardInfoIsRecommendedBoardIsTrue(String content, Pageable pageable);

    //title,gallery를 포함한 [전체] 게시물들
    Long countBoardByTitleContainingAndGallery(String title,String gallery);
    //title,gallery를 포함한 [개념글] 게시물들
    Long countBoardByTitleContainingAndGalleryAndBoardInfoIsRecommendedBoardIsTrue(String title,String gallery);
    //title를 포함한 [전체] 게시물들
    Long countBoardByTitleContaining(String title);
    //title를 포함한 [개념글] 게시물들
    Long countBoardByTitleContainingAndBoardInfoIsRecommendedBoardIsTrue(String title);
    //content,gallery를 포함한 [전체] 게시물들
    Long countBoardByContentContainingAndGallery(String content,String gallery);
    //content,gallery를 포함한 [개념글] 게시물들
    Long countBoardByContentContainingAndGalleryAndBoardInfoIsRecommendedBoardIsTrue(String content,String gallery);
    //content를 포함한 [전체] 게시물들
    Long countBoardByContentContaining(String content);
    //content를 포함한 [개념글] 게시물들
    Long countBoardByContentContainingAndBoardInfoIsRecommendedBoardIsTrue(String content);








}

