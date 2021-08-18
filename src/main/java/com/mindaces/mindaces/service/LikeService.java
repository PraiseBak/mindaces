package com.mindaces.mindaces.service;

import com.mindaces.mindaces.domain.repository.LikeRepository;
import com.mindaces.mindaces.dto.LikeDto;
import org.springframework.stereotype.Service;

@Service
public class LikeService
{
    private LikeRepository likeRepository;

    public LikeService(LikeRepository likeRepository)
    {
        this.likeRepository = likeRepository;
    }
}
