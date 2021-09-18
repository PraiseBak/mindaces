package com.mindaces.mindaces.service;


import com.mindaces.mindaces.domain.entity.Board;
import com.mindaces.mindaces.domain.entity.Likes;
import com.mindaces.mindaces.domain.repository.BoardWriteUserMapping;
import com.mindaces.mindaces.domain.repository.BoardRepository;
import com.mindaces.mindaces.domain.repository.LikesRepository;
import com.mindaces.mindaces.dto.BoardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class BoardService
{
    private static final int BLOCK_PAGE_NUM_COUNT = 10;
    private static final int PAGE_POST_COUNT = 10;

    private RoleService roleService;
    private BoardRepository boardRepository;
    private LikesRepository likesRepository;

    public BoardService(BoardRepository boardRepository,RoleService roleService,LikesRepository likesRepository)
    {
        this.roleService = roleService;
        this.boardRepository = boardRepository;
        this.likesRepository = likesRepository;
    }

    @Transactional
    public Long savePost(BoardDto boardDto)
    {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boardDto.setPassword(passwordEncoder.encode(boardDto.getPassword()));
        boardDto.setIsLoggedUser(0L);
        Board savedBoard= boardRepository.save(boardDto.toEntity());

        Likes likes = Likes.builder()
                .contentIdx(savedBoard.getContentIdx())
                .isComment(false)
                .build();
        savedBoard.setLikes(likes);
        boardRepository.save(savedBoard);
        return 0L;
    }

    public List<BoardDto> getGalleryPost(String galleryName,Integer page)
    {
        List<BoardDto> boardDtoList = new ArrayList<>();

        Page<Board> pageEntity = boardRepository.findByGallery(galleryName, PageRequest.of(page - 1, PAGE_POST_COUNT, Sort.by(Sort.Direction.DESC, "createdDate")));
        List<Board> boardEntity = pageEntity.getContent();

        if(boardEntity.isEmpty())
        {
            return boardDtoList;
        }

        for(Board board : boardEntity)
        {
            boardDtoList.add(this.convertEntityToDto(board));
        }
        return boardDtoList;
    }
    //비정상적 동작인 경우에서 자동으로 트랜잭션 기준으로 롤백해줌
    //없으면 스스로 직접 롤백해줘야함
    @Transactional
    public Long getBoardCount(String gallery)
    {
        return boardRepository.countBoardByGallery(gallery);
    }

    public Integer[] getPageList(String gallery,Integer curPage)
    {
        Integer[] pageList = new Integer[BLOCK_PAGE_NUM_COUNT];
        Double postsTotalCount = Double.valueOf(getBoardCount(gallery));
        System.out.println(postsTotalCount);
        Integer totalLastPage = (int)(Math.ceil((postsTotalCount/PAGE_POST_COUNT)));

        Integer blockLastPageNum = (totalLastPage > curPage + BLOCK_PAGE_NUM_COUNT)
                ? curPage + BLOCK_PAGE_NUM_COUNT
                : totalLastPage;

        curPage = (curPage <= 3) ? 1 : curPage - 2;

        System.out.println(curPage);
        System.out.println(totalLastPage);
        System.out.println(blockLastPageNum);


        for (int val = curPage, idx = 0; val <= blockLastPageNum; val++, idx++) {
            if(totalLastPage == 1)
            {
                break;
            }
            pageList[idx] = val;
        }

        return pageList;
    }


    private BoardDto convertEntityToDto(Board board)
    {
        return BoardDto.builder()
                .contentIdx(board.getContentIdx())
                .title(board.getTitle())
                .content(board.getContent())
                .gallery(board.getGallery())
                .user(board.getUser())
                .likes(board.getLikes())
                .isLoggedUser(board.getIsLoggedUser())
                .createdDate(board.getCreatedDate())
                .modifiedDate(board.getModifiedDate())
                .build();
    }

    public BoardDto getBoardByIdxAndGalleryName(String galleryName, Long contentIdx)
    {
        Board board =  boardRepository.findByGalleryAndContentIdx(galleryName,contentIdx,Board.class);
        if(board == null)
        {
            return null;
        }
        return this.convertEntityToDto(board);
    }

    public BoardDto getBoardInfoByGalleryAndIdx(String galleryName, Long contentIdx)
    {
        Board board =  boardRepository.findByGalleryAndContentIdx(galleryName,contentIdx,Board.class);
        return this.convertEntityToDto(board);
    }

    public Long updatePost(BoardDto boardDto,String galleryName)
    {
        Board board = (Board) boardRepository.findByGalleryAndContentIdx(galleryName,boardDto.getContentIdx(),Board.class);
        board.setContent(boardDto.getContent());
        board.setTitle((boardDto.getTitle()));
        return boardRepository.save(board).getContentIdx();
    }

    public void deletePost(Long contentIdx)
    {
        boardRepository.deleteById(contentIdx);
    }


    public Boolean isLoggedUserWriteBoard(BoardWriteUserMapping boardWriteUserMapping)
    {
        Long isLoggedUser = boardWriteUserMapping.getIsLoggedUser();
        if(isLoggedUser == 1L)
        {
            return true;
        }
        return false;
    }

    public Boolean isSameWriteUser(BoardWriteUserMapping boardWriteUserMapping,String requestUser)
    {
        String writeUser = boardWriteUserMapping.getUser();
        if(requestUser.equals(writeUser))
        {
            return true;
        }
        return false;
    }

    public BoardWriteUserMapping getBoardUserCheckMapping(String galleryName, Long contentIdx)
    {
        return boardRepository.findByGalleryAndContentIdx(galleryName,contentIdx,BoardWriteUserMapping.class);
    }

    //로그인한 케이스
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

    //비로그인한 케이스
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

            if(title.length() < 2 || title.length() > 20)
            {
                result = "제목이 2자 미만이거나 20자 초과합니다";
            }

            if(content.length() < 2 || content.getBytes().length > 65535)
            {
                result = "내용이 2자 미만이거나 65535byte를 초과합니다" + "\n현재 byte : " + Integer.toString(content.getBytes().length);
            }

            if(!isUser && isWriteMode)
            {
                if(author.length() < 2 || author.length() > 20)
                {
                    result = "글작성자가 2자 미만이거나 20자 초과합니다";
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

    public Boolean updateLikes(String mode,String gallery,Long boardIdx)
    {
        try
        {
            Likes likes = this.likesRepository.findByContentIdxAndIsComment(boardIdx,false);
            if(mode.equals("like"))
            {
                likes.updateLikes();
            }
            else
            {
                likes.updateDislikes();
            }
            likesRepository.save(likes);
        }catch (Exception e)
        {
            System.out.println(e);
            return false;
        }
        return true;
    }

    public Boolean isThereBoardInGallery(String gallery,Long boardIdx)
    {
        if(boardRepository.findByGalleryAndContentIdx(gallery,boardIdx,Board.class) == null)
        {
            return false;
        }
        return true;
    }

    private Sort likesSort()
    {
        return Sort.by(Sort.Direction.DESC, "likes.likes");
    }

    public List<BoardDto> getMostLikelyBoardList()
    {
        Sort sort = likesSort();
        //TODO likes는 likes repository에 있어야하므로 리팩토링 시작
        List<Likes> likesList = likesRepository.findTop10ByIsCommentFalseOrderByLikesDesc();
        List<Long> contentIdxList = new ArrayList<Long>();
        for(Likes like: likesList)
        {
            if(like.getLikes() != 0)
            {
                contentIdxList.add(like.getContentIdx());
            }
        }

        List<Board> boardList = boardRepository.findByContentIdxIn(contentIdxList);
        List<BoardDto> boardDtoList = new ArrayList<BoardDto>();
        for(Board board : boardList)
        {
            if(board.getTitle().length() > 20)
            {
                board.setTitle(board.getTitle().substring(19) + "...");
            }
            boardDtoList.add(this.convertEntityToDto(board));
        }
        return boardDtoList;
    }

}


