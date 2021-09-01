package com.mindaces.mindaces.service;

import com.mindaces.mindaces.domain.entity.LikedUserInfo;
import com.mindaces.mindaces.domain.repository.LikedUserInfoRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Service
public class LikedUserInfoService
{
    private LikedUserInfoRepository likedUserInfoRepository;
    private RoleService roleService;
    private BoardService boardService;

    public LikedUserInfoService(LikedUserInfoRepository likedUserInfoRepository, RoleService roleService,BoardService boardService)
    {
        this.boardService = boardService;
        this.likedUserInfoRepository = likedUserInfoRepository;
        this.roleService = roleService;
    }

    public Boolean recommand(Map<String, Object> param, HttpServletRequest request,Authentication authentication)
    {
        String gallery = (String) param.get("gallery");
        String recommendMode = (String) param.get("mode");
        Long boardIdx = (Long) param.get("boardIdx");
        String requestIP = request.getRemoteUser();
        String userName = roleService.getUserName(authentication);
        LikedUserInfo likedUserInfo;
        Boolean isSameRecommend;

        if(userName.equals("-"))
        {
            isSameRecommend = isSameIPOrUser(gallery,boardIdx,recommendMode,userName,requestIP);
        }
        else
        {
            isSameRecommend = isSameIPOrUser(gallery,boardIdx,recommendMode,userName,"-");
        }
        if(!isSameRecommend)
        {
            return false;
        }

        if(recommendMode.equals("like"))
        {
            likedUserInfo = new LikedUserInfo(gallery,boardIdx,requestIP,"-",userName);
        }
        else
        {
            likedUserInfo = new LikedUserInfo(gallery,boardIdx,"-" ,requestIP,userName);
        }

        this.likedUserInfoRepository.save(likedUserInfo);
        if(recommendMode.equals("like"))
        {
            return this.boardService.updateLikes(recommendMode,gallery,boardIdx);
        }
        return this.boardService.updateLikes(recommendMode,gallery,boardIdx);
    }

    public Boolean isSameIPOrUser(String gallery,Long boardIdx,String recommendMode,String userName,String ip)
    {
        LikedUserInfo likedUserInfo;
        if(recommendMode.equals("like"))
        {
            if(ip == null)
            {
                likedUserInfo = this.likedUserInfoRepository.findByGalleryAndBoardIdxAndUserNameAndLikedIP(gallery,boardIdx,userName,"like");
            }
            else
            {
                likedUserInfo = this.likedUserInfoRepository.findByGalleryAndBoardIdxAndLikedIP(gallery,boardIdx,ip);
            }
        }
        else
        {
            if(ip == null)
            {
                likedUserInfo = this.likedUserInfoRepository.findByGalleryAndBoardIdxAndUserNameAndDisLikedIP(gallery,boardIdx,userName,"dislike");
            }
            else
            {
                likedUserInfo = this.likedUserInfoRepository.findByGalleryAndBoardIdxAndDisLikedIP(gallery,boardIdx,ip);
            }
        }
        if(likedUserInfo == null)
        {
            return false;
        }
        return true;
    }


}
