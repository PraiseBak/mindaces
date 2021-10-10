package com.mindaces.mindaces.service;

import com.mindaces.mindaces.domain.entity.*;
import com.mindaces.mindaces.domain.repository.CommentLikeRepository;
import com.mindaces.mindaces.domain.repository.BoardLikedUserInfoRepository;
import com.mindaces.mindaces.domain.repository.LikesRepository;
import com.mindaces.mindaces.dto.BoardDto;
import com.mindaces.mindaces.dto.CommentDto;
import com.mindaces.mindaces.dto.LikeComparator;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.*;

@Service
@AllArgsConstructor
public class LikesService
{
    private CommentLikeRepository commentLikeRepository;
    private BoardLikedUserInfoRepository boardLikedUserInfoRepository;
    private RoleService roleService;
    private BoardService boardService;
    private GalleryService galleryService;
    private CommentService commentService;
    private LikesRepository likesRepository;
    private BoardSearchService boardSearchService;


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

    @Transactional
    public String recommand(Map<String, Object> param, HttpServletRequest request,Authentication authentication)
    {
        String galleryName = (String) param.get("gallery");
        String recommendMode = (String) param.get("mode");
        Long boardIdx = Long.parseLong((String) param.get("boardIdx"));
        String requestIP = request.getRemoteAddr();
        String username = roleService.getUsername(authentication);
        BoardLikedUserInfo boardLikedUserInfo;
        Boolean isSameRecommend;
        String validCheckResult = "";
        Board board;

        if(!(validCheckResult = checkRequestParamValid(galleryName,recommendMode,boardIdx) ).equals("통과"))
        {
            return validCheckResult;
        }

        if(username.equals("-"))
        {
            isSameRecommend = isSameIPOrUser(galleryName,boardIdx,recommendMode,username,requestIP);
        }
        else
        {
            requestIP = recommendMode;
            isSameRecommend = isSameIPOrUser(galleryName,boardIdx,recommendMode,username,null);
        }

        if(isSameRecommend)
        {
            return "이미 추천하였습니다";
        }

        if(recommendMode.equals("like"))
        {
            boardLikedUserInfo = new BoardLikedUserInfo(galleryName,boardIdx,requestIP,"-",username);
        }
        else
        {
            boardLikedUserInfo = new BoardLikedUserInfo(galleryName,boardIdx,"-" ,requestIP,username);
        }
        this.boardLikedUserInfoRepository.save(boardLikedUserInfo);
        Likes likes = this.getLikesOfBoard(boardIdx);
        likes.updateLike();

        /*
        //board Likes에 개추 + 1
        if(!this.updateLikes(recommendMode,likes))
        {
            return "예기지 못한 오류가 발생했습니다";
        }
         */

        board = boardSearchService.getGalleryNameAndBoardIdx(galleryName, boardIdx);
        board.getBoardLikedUserInfoList().add(boardLikedUserInfo);
        if(recommendMode.equals("like"))
        {
            boardService.updateIsRecommendBoard(galleryName,boardIdx,board);
            galleryService.updateRecommendStandard(galleryName);
        }
        return "통과";
    }



    public Boolean updateLikes(String mode,Likes likes)
    {
        if(mode.equals("like"))
        {
            likes.updateLike();
        }
        else
        {
            likes.updateDislike();
        }
        updateLikes(likes);
        return true;
    }





    public Boolean isSameIPOrUser(String gallery,Long boardIdx,String recommendMode,String username,String ip)
    {
        BoardLikedUserInfo boardLikedUserInfo;
        if(recommendMode.equals("like"))
        {
            if(ip == null)
            {
                boardLikedUserInfo = this.boardLikedUserInfoRepository.findByGalleryAndContentIdxAndUsernameAndLikedIP(gallery,boardIdx,username,"like");
            }
            else
            {
                boardLikedUserInfo = this.boardLikedUserInfoRepository.findByGalleryAndContentIdxAndLikedIP(gallery,boardIdx,ip);
            }
        }
        else
        {
            if(ip == null)
            {
                boardLikedUserInfo = this.boardLikedUserInfoRepository.findByGalleryAndContentIdxAndUsernameAndDisLikedIP(gallery,boardIdx,username,"dislike");
            }
            else
            {
                boardLikedUserInfo = this.boardLikedUserInfoRepository.findByGalleryAndContentIdxAndDisLikedIP(gallery,boardIdx,ip);
            }
        }
        if(boardLikedUserInfo == null)
        {
            return false;
        }
        return true;
    }


    public Map<String, Long> getRecentBoardLikes(String galleryName, Long boardIdx)
    {
        BoardDto boardDto = this.boardSearchService.getBoardDtoByGalleryAndIdx(galleryName,boardIdx);
        Map<String,Long> map = new HashMap<String, Long>();
        map.put("likes",boardDto.getLikes().getLike());
        map.put("dislike",boardDto.getLikes().getDislike());
        return map;
    }

    private CommentLikedUserInfo commentLikeEntityBuild(Long commentIdx, String requestIP, String username)
    {
        return CommentLikedUserInfo.builder()
                .commentIdx(commentIdx)
                .likedIP(requestIP)
                .username(username)
                .build();
    }


    @Transactional
    public String commentRecommand(Map<String, Object> param, HttpServletRequest request, Authentication authentication)
    {
        Long commentIdx = Long.parseLong((String) param.get("contentIdx"));
        String requestIP = request.getRemoteAddr();
        String username = roleService.getUsername(authentication);
        Boolean isSameRecommend;
        String validCheckResult = "통과";
        Comment comment;
        CommentLikedUserInfo commentLikedUserInfo;
        isSameRecommend = checkDupliComment(commentIdx,requestIP,username);

        if(isSameRecommend)
        {
            return "이미 추천하였습니다";
        }

        commentLikedUserInfo = commentLikeEntityBuild(commentIdx,requestIP,username);
        commentLikeRepository.save(commentLikedUserInfo);

        if(!commentService.updateLikes(commentIdx))
        {
            return "예기지 못한 오류가 발생했습니다";
        }

        comment = commentService.getCommentByID(commentIdx);
        comment.getCommentLikedUserInfoList().add(commentLikedUserInfo);
        commentService.saveComment(comment);

        return validCheckResult;
    }

    private Boolean checkDupliComment(Long commentIdx,String likedIP,String username)
    {
        CommentLikedUserInfo resultCommentLikedUserInfo;
        if(!username.equals("-"))
        {
            resultCommentLikedUserInfo = commentLikeRepository.findByCommentIdxAndUsername(commentIdx,username);
        }
        else
        {
            resultCommentLikedUserInfo = commentLikeRepository.findByCommentIdxAndLikedIP(commentIdx,likedIP);
        }

        if(resultCommentLikedUserInfo == null)
        {
            return false;
        }
        return true;
    }

    public Map<String, Long> getRecentCommentLikes(Long commentIdx)
    {
        Comment comment = this.commentService.getCommentByID(commentIdx);
        Map<String,Long> map = new HashMap<String, Long>();
        map.put("likes",comment.getLikes().getLike());
        return map;
    }

    public List<CommentDto> getMostLikedCommentList(List<CommentDto> commentDtoList)
    {

        List<CommentDto> tmpCommentDtoList = new ArrayList<>();
        List<CommentDto> mostLikedCommentDtoList = new ArrayList<>();
        int count = 0;

        tmpCommentDtoList.addAll(commentDtoList);

        tmpCommentDtoList.sort(new LikeComparator<CommentDto>());

        for (CommentDto commentDto : tmpCommentDtoList)
        {
            if(count == 3 || commentDto.getLikes().getLike() == 0)
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

    public Likes getLikesOfBoard(Long boardIdx)
    {
        return this.likesRepository.findByContentIdxAndIsComment(boardIdx,false);
    }

    public void updateLikes(Likes likes)
    {
        likesRepository.save(likes);
    }


    public void deleteBoardLikesByBoardIdx(Long contentIdx)
    {
        this.likesRepository.deleteByContentIdxAndIsComment(contentIdx,false);
    }

    public void deleteCommentLikesByBoardIdx(Long contentIdx)
    {
        this.likesRepository.deleteByContentIdxAndIsComment(contentIdx,true);
    }
}
