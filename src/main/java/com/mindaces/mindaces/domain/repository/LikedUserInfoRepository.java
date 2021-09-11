package com.mindaces.mindaces.domain.repository;

import com.mindaces.mindaces.domain.entity.LikedUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikedUserInfoRepository extends JpaRepository<LikedUserInfo, Long>
{
    LikedUserInfo findByGalleryAndBoardIdxAndDisLikedIP(String gallery, Long boardIdx,String ip);
    LikedUserInfo findByGalleryAndBoardIdxAndLikedIP(String gallery, Long boardIdx, String ip);
    LikedUserInfo findByGalleryAndBoardIdxAndUserNameAndLikedIP(String gallery, Long boardIdx,String userName,String ip);
    LikedUserInfo findByGalleryAndBoardIdxAndUserNameAndDisLikedIP(String gallery, Long boardIdx,String userName,String ip);

}
