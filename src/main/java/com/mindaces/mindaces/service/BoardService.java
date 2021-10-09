package com.mindaces.mindaces.service;


import com.mindaces.mindaces.domain.entity.Board;
import com.mindaces.mindaces.domain.entity.BoardInfo;
import com.mindaces.mindaces.domain.entity.Likes;
import com.mindaces.mindaces.domain.repository.BoardWriteUserMapping;
import com.mindaces.mindaces.domain.repository.BoardRepository;
import com.mindaces.mindaces.dto.BoardDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class BoardService
{

    private GalleryService galleryService;
    private RoleService roleService;
    private CommentService commentService;
    private BoardRepository boardRepository;
    private UtilService utilService;
    private BoardSearchService boardSearchService;

    @Transactional
    public Long savePost(BoardDto boardDto)
    {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boardDto.setPassword(passwordEncoder.encode(boardDto.getPassword()));

        boardDto.setTitle(utilService.getTrimedStr(boardDto.getTitle()));

        Board savedBoard= boardRepository.save(boardDto.toEntity());
        Likes likes = Likes.builder()
                .contentIdx(savedBoard.getContentIdx())
                .isComment(false)
                .build();

        BoardInfo boardInfo = new BoardInfo();
        boardInfo.updateGallery(savedBoard.getGallery());
        boardInfo.updateContentIdx(savedBoard.getContentIdx());

        savedBoard.updateBoardInfo(boardInfo);
        savedBoard.updateLikes(likes);
        boardRepository.save(savedBoard);
        return 0L;
    }

    //글 수정
    public Long updatePost(BoardDto boardDto,String galleryName)
    {
        Board board = (Board) boardRepository.findByGalleryAndContentIdx(galleryName,boardDto.getContentIdx(),Board.class);
        board.updateContent(boardDto.getContent());
        board.updateTitle(boardDto.getTitle());
        return boardRepository.save(board).getContentIdx();
    }

    //글 삭제
    public void deletePost(Long contentIdx)
    {
        boardRepository.deleteById(contentIdx);
    }

    //로그인한 유저가 작성한 글인지 확인
    public Boolean isLoggedUserWriteBoard(BoardWriteUserMapping boardWriteUserMapping)
    {
        Long isLoggedUser = boardWriteUserMapping.getIsLoggedUser();
        if(isLoggedUser == 1L)
        {
            return true;
        }
        return false;
    }

    //동일한 유저인지 체크
    public Boolean isSameWriteUser(BoardWriteUserMapping boardWriteUserMapping,String requestUser)
    {
        String writeUser = boardWriteUserMapping.getUser();
        if(requestUser.equals(writeUser))
        {
            return true;
        }
        return false;
    }

    //로그인한 케이스의 게시글 수정 유효성 체크
    public Boolean isBoardModifyAuthValidLoggedUser(Authentication authentication, Long contentIdx, String galleryName)
    {
        BoardWriteUserMapping boardWriteUserMapping = boardRepository.findByGalleryAndContentIdx(galleryName,contentIdx, BoardWriteUserMapping.class);
        if(boardWriteUserMapping != null)
        {
            if(isLoggedUserWriteBoard(boardWriteUserMapping))
            {
                if(roleService.isUser(authentication))
                {
                    if(isSameWriteUser(boardWriteUserMapping,authentication.getName()))
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //비로그인한 케이스의 게시글 유효성 체크
    public Boolean isBoardModifyAuthValidUser(Long contentIdx, String inputPassword,String galleryName)
    {
        BoardWriteUserMapping boardWriteUserMapping = boardRepository.findByGalleryAndContentIdx(galleryName,contentIdx, BoardWriteUserMapping.class);
        if(boardWriteUserMapping.getIsLoggedUser() == 0L)
        {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            return passwordEncoder.matches(inputPassword,boardWriteUserMapping.getPassword());
        }
        return false;
    }

    //게시글의 유효성 체크
    public String isBoardWriteValid(BoardDto boardDto, Authentication authentication,String mode)
    {
        try
        {
            String title = boardDto.getTitle();
            String content = boardDto.getContent();
            String author = boardDto.getUser();
            String password = boardDto.getPassword();
            Boolean isUser = this.roleService.isUser(authentication);
            Boolean isWriteMode = mode.equals("write");
            String result = "통과";

            if(utilService.isLRWhiteSpace(author))
            {
                return "닉네임의 앞 뒤에는 공백이 올 수 없습니다";
            }

            if(utilService.isThereWhiteSpace(password))
            {
                return "비밀번호에는 공백이 올 수 없습니다";
            }

            if(title.length() < 2 || title.length() > 20)
            {
                result = "제목이 2자 미만이거나 20자 초과합니다";
            }

            if(content.length() < 2 || content.getBytes().length > 65535)
            {
                result = "내용이 2자 미만이거나 65535byte를 초과합니다";
                if(content.getBytes().length > 65535)
                {
                    result += "\n현재 byte : " + content.getBytes().length;
                }
            }

            if(!isUser && isWriteMode)
            {
                if(author.length() < 2 || author.length() > 20)
                {
                    result = "글쓴이가 2자 미만이거나 20자 초과합니다";
                }
            }

            if(!isUser && isWriteMode)
            {
                if(password.length() < 4 || password.length() > 20)
                {
                    result = "비밀번호가 4자 미만이거나 20자 초과합니다";
                }
            }
            return result;
        }
        catch (Exception e)
        {
            return "예기치 못한 에러입니다";
        }
    }


    //개념글 갱신
    public void updateIsRecommendBoard(String galleryName, Long boardIdx,Board board)
    {
        Long recommendStandard;
        Long boardRecommend;
        Long countComment;

        recommendStandard = this.galleryService.getRecommendStandard(galleryName);
        //이미 개추 +1 한걸 가져옴
        boardRecommend = board.getLikes().getLike();
        //보드 개추
        countComment = this.commentService.countByBoardIdx(boardIdx);
        //댓글 몇개인지 체크하여 개추여부 수정


        //이미 개념글 그냥 기준 무시하고 + 1
        if(board.getBoardInfo().getIsRecommendedBoard())
        {
            galleryService.updateRecommendInfo(galleryName,boardRecommend,!(board.getBoardInfo().getIsRecommendedBoard()));
        }
        //처음 개추글
        else if(boardRecommend >= recommendStandard && countComment >= recommendStandard / 3)
        {
            galleryService.updateRecommendInfo(galleryName,boardRecommend,!(board.getBoardInfo().getIsRecommendedBoard()));
            board.getBoardInfo().updateIsRecommendedBoard();
        }
    }

    //조회수 증가
    public void addVisitedNum(Long boardIdx)
    {
        Board board = this.boardRepository.getById(boardIdx);
        board.getBoardInfo().updateVisitedNum();
        this.boardRepository.save(board);
    }

    //해당 갤러리에 해당 게시물이 존재하는지
    public Boolean isThereBoardInGallery(String gallery,Long boardIdx)
    {
        if(boardRepository.findByGalleryAndContentIdx(gallery,boardIdx,Board.class) == null)
        {
            return false;
        }
        return true;
    }

}



