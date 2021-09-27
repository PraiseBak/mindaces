package com.mindaces.mindaces.service;

import com.mindaces.mindaces.domain.repository.BoardLikedUserInfoRepository;
import org.springframework.stereotype.Service;

@Service
public class BoardLikedUserInfoService
{
    BoardLikedUserInfoRepository boardLikedUserInfoRepository;
    public BoardLikedUserInfoService(BoardLikedUserInfoRepository boardLikedUserInfoRepository)
    {
        this.boardLikedUserInfoRepository = boardLikedUserInfoRepository;
    }

    public void deleteLikedUserInfoByBoardIdx(Long idx)
    {
        this.boardLikedUserInfoRepository.deleteByContentIdx(idx);
    }


}
