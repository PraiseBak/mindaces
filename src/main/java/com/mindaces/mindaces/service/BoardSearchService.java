package com.mindaces.mindaces.service;

import com.mindaces.mindaces.domain.entity.Board;
import com.mindaces.mindaces.domain.repository.BoardRepository;
import com.mindaces.mindaces.dto.BoardDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;


/*
Board 내용을 키워드를 이용해 가져오거나 보여주는 것에 중점을 둔 서비스
Board 내용의 수정 및 저장 등은 BoardService에서함
*/
@Service
@AllArgsConstructor
public class BoardSearchService
{
    private static final int BLOCK_PAGE_NUM_COUNT = 10;
    private static final int PAGE_POST_COUNT = 10;
    private BoardRepository boardRepository;
    private GalleryService galleryService;

    //search에서 페이지를 옵션에 맞춰 가져옴
    //title로 검색,content로 검색,특정 gallery에 속하는 글만 검색,추천글만 검색
    private Page<Board> getPageEntityBySearchOption(String title, String content, Boolean isRecommendedBoard, Integer page, String gallerySerachKeyword)
    {
        Page<Board> pageEntity;
        Boolean isTitleSearch = title != null;
        Boolean isGallerySearch = gallerySerachKeyword != null;
        Pageable pageable = PageRequest.of(page - 1, PAGE_POST_COUNT, Sort.by(Sort.Direction.DESC, "createdDate"));

        if(isTitleSearch)
        {
            if(isGallerySearch)
            {
                if(isRecommendedBoard)
                {
                    pageEntity = this.boardRepository.findByTitleContainingAndGalleryAndBoardInfoIsRecommendedBoardIsTrue(title,pageable,gallerySerachKeyword);
                }
                else
                {
                    pageEntity = this.boardRepository.findByTitleContainingAndGallery(title, pageable,gallerySerachKeyword );
                }
            }
            else
            {
                if(isRecommendedBoard)
                {
                    pageEntity = this.boardRepository.findByTitleContainingAndBoardInfoIsRecommendedBoardIsTrue(title, pageable);
                }
                else
                {
                    pageEntity = this.boardRepository.findByTitleContaining(title, pageable);
                }
            }
        }
        //내용
        else
        {
            if(isGallerySearch)
            {
                if(isRecommendedBoard)
                {
                    pageEntity = this.boardRepository.findByContentContainingAndGalleryAndBoardInfoIsRecommendedBoardIsTrue(content, pageable,gallerySerachKeyword);
                }
                else
                {
                    pageEntity = this.boardRepository.findByContentContainingAndGallery(content, pageable,gallerySerachKeyword);
                }
            }
            else
            {
                if(isRecommendedBoard)
                {
                    pageEntity = this.boardRepository.findByContentContainingAndBoardInfoIsRecommendedBoardIsTrue(content, pageable);
                }
                else
                {
                    pageEntity = this.boardRepository.findByContentContaining(content, pageable);
                }
            }
        }
        return pageEntity;
    }


    //검색페이지에서 해당 옵션에 맞춰서 boardList 가져옴
    public List<BoardDto> getBoardListBySearchKeyword(String keyword, String searchMode, Integer page, Boolean isRecommendedBoardMode, String gallerySearchKeyword)
    {
        List<BoardDto> boardDtoList = new ArrayList<>();
        Page<Board> pageEntity;

        if(searchMode.equals("title"))
        {
            pageEntity = getPageEntityBySearchOption(keyword,null,isRecommendedBoardMode,page,gallerySearchKeyword);
        }
        else
        {
            pageEntity = getPageEntityBySearchOption(null,keyword,isRecommendedBoardMode,page,gallerySearchKeyword);
        }

        List<Board> boardList = pageEntity.getContent();

        for(Board board : boardList)
        {
            boardDtoList.add(this.convertEntityToDto(board));
        }

        return boardDtoList;
    }

    //Entity To Dto
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
                .boardInfo(board.getBoardInfo())
                .build();
    }

    //유저가 작성한 글 가져오기 (유저 검색)
    public void addingPagedBoardToModelByWritedUser(Model model, int page, String pagingMode, String username)
    {
        List<BoardDto> boardDtoList;
        Long count;
        Integer[] pageList;

        //유저가 작성한 글 중 개념글인 글들만 가져오기
        if(pagingMode.equals("mostLikedBoard"))
        {
            boardDtoList = getMostLikelyBoardListByFindObj(username,page,"mypage");
            count = getCountRecommendedBoardByUsername(username);
            pageList = getPageList(page,count);
        }
        //유저가 작성한 전체 글
        else
        {
            boardDtoList = getPostByFindObj(username,page,"mypage");
            count = getCountBoardByUsername(username);
            pageList = getPageList(page,count);
        }

        model.addAttribute("postList",boardDtoList);
        model.addAttribute("pageList",pageList);
    }


    //유저가 작성한 글 갯수
    private Long getCountBoardByUsername(String username)
    {
        return this.boardRepository.countBoardByUserAndIsLoggedUser(username,1L);
    }

    //유저가 작성한 글 중 개념글
    private Long getCountRecommendedBoardByUsername(String username)
    {
        return this.boardRepository.countBoardByUserAndBoardInfoIsRecommendedBoardIsTrueAndIsLoggedUser(username,1L);
    }


    //유저검색의 경우에 게시물들 가져오거나
    //그냥 일반 갤러리에서의 게시물들 가져오기
    public List<BoardDto> getPostByFindObj(String findObj, Integer page, String mode)
    {
        List<BoardDto> boardDtoList = new ArrayList<>();
        Page<Board> pageEntity;

        if(mode.equals("mypage"))
        {
            pageEntity = boardRepository.findByUser(findObj,
                    PageRequest.of(page - 1, PAGE_POST_COUNT, Sort.by(Sort.Direction.DESC, "createdDate")));
        }
        else
        {
            pageEntity = boardRepository.findByGallery(findObj,
                    PageRequest.of(page - 1, PAGE_POST_COUNT, Sort.by(Sort.Direction.DESC, "createdDate")));
        }

        List<Board> boardList = pageEntity.getContent();

        if(boardList.isEmpty())
        {
            return boardDtoList;
        }

        for(Board board : boardList)
        {
            boardDtoList.add(this.convertEntityToDto(board));
        }
        return boardDtoList;
    }

    //전체 갤러리에서 인기글 (메인 컨트롤러에서 조회)
    public List<BoardDto> getMostLikelyBoardList()
    {
        List<Board> boardList = boardRepository.findTop10ByOrderByLikesLikeDesc();
        List<BoardDto> boardDtoList = new ArrayList<BoardDto>();
        for(Board board : boardList)
        {
            if(board.getLikes().getLike() == 0L)
            {
                break;
            }
            if(board.getTitle().length() > 20)
            {
                board.updateTitle(board.getTitle().substring(19) + "...");
            }
            boardDtoList.add(this.convertEntityToDto(board));
        }
        return boardDtoList;
    }

    //해당 갤러리에서의 개념글들 가져오기
    public List<BoardDto> getMostLikelyBoardListByFindObj(String findObj,Integer page,String mode)
    {
        List<BoardDto> boardDtoList = new ArrayList<>();
        //갤러리에 개념글 기준 개추 수록
        //개추는 최소 10 이상
        //댓글은 개추 / 3 이어야함
        //댓글이 총 몇개인지 가져와야함

        Page<Board> pageEntity;
        if(mode.equals("mypage"))
        {
            pageEntity = this.boardRepository.findByUserAndBoardInfoIsRecommendedBoardIsTrue(findObj,
                    PageRequest.of(page - 1, PAGE_POST_COUNT, Sort.by(Sort.Direction.DESC, "createdDate")));
        }
        else
        {
            pageEntity = this.boardRepository.findByGalleryAndBoardInfoIsRecommendedBoardIsTrue(findObj,
                    PageRequest.of(page - 1, PAGE_POST_COUNT, Sort.by(Sort.Direction.DESC, "createdDate")));
        }


        List<Board> boardList = pageEntity.getContent();

        for(Board board : boardList)
        {
            boardDtoList.add(this.convertEntityToDto(board));
        }

        return boardDtoList;
    }


    //주어진 모델에 페이징된 게시물들 및 버튼 추가 (해당 갤러리의 전체 게시물 contentList)
    public void addingPagedBoardToModel(Model model, String galleryName, int page, String pagingMode)
    {
        List<BoardDto> boardDtoList;
        Long count;
        Integer[] pageList;

        if(pagingMode.equals("mostLikedBoard"))
        {
            boardDtoList = getMostLikelyBoardListByFindObj(galleryName,page, "board");
            count = galleryService.getCountRecommendedBoardByGalleryName(galleryName);
            pageList = getPageList(page,count);
        }
        else
        {
            boardDtoList = getPostByFindObj(galleryName,page,"board");
            count = getCountBoardByGallery(galleryName);
            pageList = getPageList(page,count);
        }
        model.addAttribute("pageList",pageList);
        model.addAttribute("postList",boardDtoList);
    }


    //갤러리가 가지고 있는 게시물 갯수 조회
    public Long getCountBoardByGallery(String galleryName)
    {
        return this.boardRepository.countBoardByGallery(galleryName);
    }

    //주어진 모델에 페이징된 게시물들 및 버튼 추가 (search에서 조회)
    public void addingPagedBoardToModelByKeyword(Model model, String keyword, String searchMode,Integer page, String pagingMode,String searchGalleryKeyword)
    {
        List<BoardDto> boardDtoList;
        Long count;
        Integer[] pageList;
        Boolean isRecommendedBoardMode = pagingMode.equals("mostLikedBoard");
        //getBoardListBySearchKeyword 사용하는 다른 메소드들 확인
        boardDtoList = getBoardListBySearchKeyword(keyword,searchMode,page, isRecommendedBoardMode,searchGalleryKeyword);
        //getCountBoardByKeyword에서 Gallery 키워드 사용하도록 수정해야함
        count = getCountBoardByKeyword(keyword,searchMode,searchGalleryKeyword,isRecommendedBoardMode);
        pageList = getPageList(page,count);
        model.addAttribute("pageList",pageList);
        model.addAttribute("postList",boardDtoList);
    }

    //search 페이지에서 키워드들 포함하는 게시물 갯수 조회
    public Long getCountBoardByKeyword(String keyword,String searchMode,String searchGalleryKeyword,Boolean isRecommendedBoard)
    {
        Long count;
        if(searchGalleryKeyword == null)
        {
            if(searchMode.equals("title"))
            {
                if(isRecommendedBoard)
                {
                    count = this.boardRepository.countBoardByTitleContainingAndBoardInfoIsRecommendedBoardIsTrue(keyword);
                }
                else
                {
                    count = this.boardRepository.countBoardByTitleContaining(keyword);
                }
            }
            else
            {
                if(isRecommendedBoard)
                {
                    count = this.boardRepository.countBoardByContentContainingAndBoardInfoIsRecommendedBoardIsTrue(keyword);
                }
                else
                {
                    count = this.boardRepository.countBoardByContentContaining(keyword);
                }
            }

        }
        else
        {
            if(searchMode.equals("title"))
            {
                if(isRecommendedBoard)
                {
                    count = this.boardRepository.countBoardByTitleContainingAndGalleryAndBoardInfoIsRecommendedBoardIsTrue(keyword,searchGalleryKeyword);
                }
                else
                {
                    count = this.boardRepository.countBoardByTitleContainingAndGallery(keyword,searchGalleryKeyword);
                }
            }
            else
            {
                if(isRecommendedBoard)
                {
                    count = this.boardRepository.countBoardByContentContainingAndGalleryAndBoardInfoIsRecommendedBoardIsTrue(keyword,searchGalleryKeyword);
                }
                else
                {
                    count = this.boardRepository.countBoardByContentContainingAndGallery(keyword,searchGalleryKeyword);
                }
            }
        }
        return count;
    }



    //갤러리와 id와 일치하는 BoardDto 조회
    public BoardDto getBoardDtoByGalleryAndIdx(String galleryName, Long contentIdx)
    {
        Board board =  boardRepository.findByGalleryAndContentIdx(galleryName,contentIdx,Board.class);
        return this.convertEntityToDto(board);
    }

    //갤러리와 id와 일치하는 Board 조회
    public Board getGalleryNameAndBoardIdx(String galleryName, Long contentIdx)
    {
        Board board =  boardRepository.findByGalleryAndContentIdx(galleryName,contentIdx,Board.class);
        if(board == null)
        {
            return null;
        }
        return board;
    }

    //page 버튼을 조회
    public Integer[] getPageList(int curPage,Long count)
    {
        Integer[] pageList = new Integer[BLOCK_PAGE_NUM_COUNT];
        Double postsTotalCount = Double.valueOf(count);
        Integer totalLastPageNum = (int)(Math.ceil((postsTotalCount/ PAGE_POST_COUNT)));
        Integer blockStartPageNum =
                (curPage <= BLOCK_PAGE_NUM_COUNT / 2)
                        ? 1
                        : curPage - BLOCK_PAGE_NUM_COUNT / 2;
        Integer blockLastPageNum =
                (totalLastPageNum > blockStartPageNum + BLOCK_PAGE_NUM_COUNT - 1 )
                        ? blockStartPageNum + BLOCK_PAGE_NUM_COUNT - 1
                        : totalLastPageNum;


        for (int val = blockStartPageNum, idx = 0; val <= blockLastPageNum; val++, idx++) {
            pageList[idx] = val;
            if(totalLastPageNum == 1)
            {
                break;
            }
        }
        return pageList;
    }

    public String getBoardTitleByBoardIdxAndGallery(Long boardIdx, String gallery)
    {
        BoardDto board;
        board = this.getBoardDtoByGalleryAndIdx(gallery,boardIdx);
        return board.getTitle();
    }
}
