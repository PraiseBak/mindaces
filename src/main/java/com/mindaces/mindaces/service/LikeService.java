package com.mindaces.mindaces.service;

import com.mindaces.mindaces.domain.entity.Comment;
import com.mindaces.mindaces.domain.entity.CommentLikeUserInfo;
import com.mindaces.mindaces.domain.entity.LikedUserInfo;
import com.mindaces.mindaces.domain.repository.CommentLikeRepository;
import com.mindaces.mindaces.domain.repository.LikedUserInfoRepository;
import com.mindaces.mindaces.dto.BoardDto;
import com.mindaces.mindaces.dto.CommentDto;
import com.mindaces.mindaces.dto.LikeComparator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class LikeService
{
    private CommentLikeRepository commentLikeRepository;
    private LikedUserInfoRepository likedUserInfoRepository;
    private RoleService roleService;
    private BoardService boardService;
    private GalleryService galleryService;
    private CommentService commentService;


    public LikeService(LikedUserInfoRepository likedUserInfoRepository, RoleService roleService, BoardService boardService, GalleryService galleryService,CommentLikeRepository commentLikeRepository,CommentService commentService)
    {
        this.commentService = commentService;
        this.boardService = boardService;
        this.likedUserInfoRepository = likedUserInfoRepository;
        this.roleService = roleService;
        this.galleryService = galleryService;
        this.commentLikeRepository = commentLikeRepository;
    }

    public Boolean isRecommendModeOk(String mode)
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

        if(!isRecommendModeOk(recommendMode))
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


    public Map<String, Long> getRecentCommentLikes(String galleryName, Long boardIdx)
    {
        BoardDto boardDto = this.boardService.getBoardByIdxAndGalleryName(galleryName,boardIdx);
        Map<String,Long> map = new HashMap<String, Long>();
        map.put("likes",boardDto.getLikes().getLikes());
        map.put("dislikes",boardDto.getLikes().getDislikes());
        return map;
    }

    private CommentLikeUserInfo commentLikeEntityBuild(Long commentIdx, String requestIP, String userName)
    {
        CommentLikeUserInfo commentLikeUserInfo = CommentLikeUserInfo.builder()
                .commentIdx(commentIdx)
                .likedIP(requestIP)
                .userName(userName)
                .build();
        return commentLikeUserInfo;
    }



    public String commentRecommand(Map<String, Object> param, HttpServletRequest request, Authentication authentication)
    {
        Long commentIdx = Long.parseLong((String) param.get("commentIdx"));
        String requestIP = request.getRemoteAddr();
        String userName = roleService.getUserName(authentication);
        Boolean isSameRecommend;
        String validCheckResult = "통과";
        CommentLikeUserInfo commentLikeUserInfo;
        isSameRecommend = checkDupliComment(commentIdx,requestIP,userName);

        if(isSameRecommend)
        {
            return "이미 추천하였습니다";
        }

        commentLikeUserInfo = commentLikeEntityBuild(commentIdx,requestIP,userName);

        commentLikeRepository.save(commentLikeUserInfo);

        if(!commentService.updateLikes(commentIdx))
        {
            return "예기지 못한 오류가 발생했습니다";
        }

        return validCheckResult;
    }

    private Boolean checkDupliComment(Long commentIdx,String likedIP,String userName)
    {
        CommentLikeUserInfo resultCommentLikeUserInfo;
        if(!userName.equals("-"))
        {
            resultCommentLikeUserInfo = commentLikeRepository.findByCommentIdxAndUserName(commentIdx,userName);
        }
        else
        {
            resultCommentLikeUserInfo = commentLikeRepository.findByCommentIdxAndLikedIP(commentIdx,likedIP);
        }

        if(resultCommentLikeUserInfo == null)
        {
            return false;
        }
        return true;
    }

    public Map<String, Long> getRecentCommentLikes(Long commentIdx)
    {
        Comment comment = this.commentService.getCommentByID(commentIdx);
        Map<String,Long> map = new HashMap<String, Long>();
        map.put("likes",comment.getLikes().getLikes());
        return map;
    }

    public List<CommentDto> getMostLikedCommentList(List<CommentDto> commentDtoList)
    {

        List<CommentDto> tmpCommentDtoList = new ArrayList<>();
        List<CommentDto> mostLikedCommentDtoList = new ArrayList<>();
        int count = 0;

        for (CommentDto commentDto : commentDtoList)
        {
            tmpCommentDtoList.add(commentDto);
        }

        tmpCommentDtoList.sort(new LikeComparator<CommentDto>());

        for (CommentDto commentDto : tmpCommentDtoList)
        {
            if(count == 3 || commentDto.getLikes().getLikes() == 0)
            {
                break;
            }
            mostLikedCommentDtoList.add(commentDto);
            count++;
        }
        if(count == 0)
        {
            return null;
        }
        return mostLikedCommentDtoList;
    }

}
