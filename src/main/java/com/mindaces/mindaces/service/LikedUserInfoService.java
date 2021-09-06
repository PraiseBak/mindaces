package com.mindaces.mindaces.service;

import com.mindaces.mindaces.domain.entity.LikedUserInfo;
import com.mindaces.mindaces.domain.repository.LikedUserInfoRepository;
import com.mindaces.mindaces.dto.BoardDto;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Service
public class LikedUserInfoService
{
    private LikedUserInfoRepository likedUserInfoRepository;
    private RoleService roleService;
    private BoardService boardService;
    private GalleryService galleryService;


    public LikedUserInfoService(LikedUserInfoRepository likedUserInfoRepository, RoleService roleService,BoardService boardService,GalleryService galleryService)
    {
        this.boardService = boardService;
        this.likedUserInfoRepository = likedUserInfoRepository;
        this.roleService = roleService;
        this.galleryService = galleryService;
    }

    public Boolean isRecommandModeOk(String mode)
    {
        if(mode.equals("like"))
        {
            return true;
        }
        if(mode.equals("dislike"))
        {
            return true;
        }
        return false;
    }

    public String checkRequestParamValid(String gallery,String recommendMode,Long boardIdx)
    {
        if(!galleryService.isGallery(gallery))
        {
            return "존재하지 않는 갤러리입니다";
        }

        if(!isRecommandModeOk(recommendMode))
        {
            return "예기치 못한 에러입니다";
        }

        if(!boardService.isThereBoardInGallery(gallery,boardIdx))
        {
            return "예기치 못한 에러입니다";
        }
        return "통과";
    }


    public String recommand(Map<String, Object> param, HttpServletRequest request,Authentication authentication)
    {
        String gallery = (String) param.get("gallery");
        String recommendMode = (String) param.get("mode");
        Long boardIdx = Long.parseLong((String) param.get("boardIdx"));
        String requestIP = request.getRemoteAddr();
        String userName = roleService.getUserName(authentication);
        LikedUserInfo likedUserInfo;
        Boolean isSameRecommend;
        String validCheckResult = "";

        if(!(validCheckResult = checkRequestParamValid(gallery,recommendMode,boardIdx) ).equals("통과"))
        {
            return validCheckResult;
        }

        if(userName.equals("-"))
        {
            isSameRecommend = isSameIPOrUser(gallery,boardIdx,recommendMode,userName,requestIP);
        }
        else
        {
            requestIP = recommendMode;
            isSameRecommend = isSameIPOrUser(gallery,boardIdx,recommendMode,userName,null);
        }

        if(isSameRecommend)
        {
            return "이미 추천하였습니다";
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

        if(!boardService.updateLikes(recommendMode,gallery,boardIdx))
        {
            return "예기지 못한 오류가 발생했습니다";
        }
        return "통과";
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


    public Map<String, Long> getRecentLikes(String galleryName, Long boardIdx)
    {
        System.out.println(galleryName + " " + boardIdx);
        BoardDto boardDto = this.boardService.getBoardByIdxAndGalleryName(galleryName,boardIdx);
        Map<String,Long> map = new HashMap<String, Long>();
        map.put("likes",boardDto.getLikes());
        map.put("dislikes",boardDto.getDislikes());
        return map;
    }
}