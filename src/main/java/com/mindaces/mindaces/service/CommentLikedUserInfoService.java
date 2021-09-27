package com.mindaces.mindaces.service;

import com.mindaces.mindaces.domain.repository.CommentLikedUserInfoRepository;
import org.springframework.stereotype.Service;

//TODO CommentLikedUserInfoRepository add
@Service
public class CommentLikedUserInfoService
{
    CommentLikedUserInfoRepository commentLikedUserInfoRepository;

    public CommentLikedUserInfoService(CommentLikedUserInfoRepository commentLikedUserInfoRepository)
    {
        this.commentLikedUserInfoRepository = commentLikedUserInfoRepository;
    }
}
