package com.mindaces.mindaces.domain.repository;

import com.mindaces.mindaces.domain.entity.BoardLikedUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardLikedUserInfoRepository extends JpaRepository<BoardLikedUserInfo, Long>
{
    BoardLikedUserInfo findByGalleryAndContentIdxAndDisLikedIP(String gallery, Long boardIdx, String ip);
    BoardLikedUserInfo findByGalleryAndContentIdxAndLikedIP(String gallery, Long boardIdx, String ip);
    BoardLikedUserInfo findByGalleryAndContentIdxAndUserNameAndLikedIP(String gallery, Long boardIdx, String userName, String ip);
    BoardLikedUserInfo findByGalleryAndContentIdxAndUserNameAndDisLikedIP(String gallery, Long boardIdx, String userName, String ip);
    void deleteByContentIdx(Long boardIdx);
}
