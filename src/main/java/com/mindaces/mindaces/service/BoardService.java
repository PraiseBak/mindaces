package com.mindaces.mindaces.service;


import com.mindaces.mindaces.domain.entity.Board;
import com.mindaces.mindaces.domain.repository.BoardRepository;
import com.mindaces.mindaces.domain.repository.GalleryRepository;
import com.mindaces.mindaces.dto.BoardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BoardService
{
    private BoardRepository boardRepository;
    private GalleryRepository galleryRepository;

    public BoardService(BoardRepository boardRepository)
    {
        this.boardRepository = boardRepository;
    }

    @Transactional
    public Long savePost(BoardDto boardDto)
    {
        return boardRepository.save(boardDto.toEntity()).getContentIdx();
    }

    public Page<BoardDto> paging(@PageableDefault(sort="createdDate") Pageable pageRequest)
    {
        Page<Board> boardList = boardRepository.findAll(pageRequest);
        //TODO 페이징
        /*
        Page<BoardDto> pagingList = boardList.map(
                post -> new BoardDto(
                            post.getGallery(),post.getUser(),
                            post.getContentIdx(),post.getContent(),
                            post.getTitle()//,post.getCreatedDate(),post.getModifiedDate()
                        ));

        for (BoardDto paging : pagingList)
        {
            System.out.println(paging.getTitle());
            System.out.println(paging.getContent());
            //System.out.println(paging.getCreatedDate());
        }
         */
        return null;
        //return pagingList;
    }


    public List<BoardDto> getGalleryPost(String galleryName)
    {
        List<BoardDto> boardDtoList = new ArrayList<>();
        List<Board> boardEntity = boardRepository.findByGalleryContaining(galleryName);

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

    private BoardDto convertEntityToDto(Board board)
    {
        return BoardDto.builder()
                .contentIdx(board.getContentIdx())
                .title(board.getTitle())
                .content(board.getContent())
                .gallery(board.getGallery())
                .user(board.getUser())
                .likes(board.getLikes())
                .disLikes(board.getDisLikes())
                .build();

    }


    public BoardDto getBoardByIdx(Long contentIndex)
    {
        Board board = boardRepository.findById(contentIndex).get();
        return this.convertEntityToDto(board);
    }

    public BoardDto getBoardInfoByGalleryAndIdx(String galleryName, Long contentIdx)
    {
        Board board =  boardRepository.findByGalleryAndContentIdx(galleryName,contentIdx);
        return this.convertEntityToDto(board);
    }

    public Long updatePost(BoardDto boardDto)
    {
        Board board = boardRepository.getById(boardDto.getContentIdx());
        board.setContent(boardDto.getContent());
        board.setTitle((boardDto.getTitle()));
        return boardRepository.save(board).getContentIdx();
    }

    public void deletePost(Long contentIdx)
    {
        boardRepository.deleteById(contentIdx);
    }

    public Boolean checkPassword(Long contentIdx, String inputPassword)
    {
        Board board = boardRepository.findById(contentIdx).get();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(inputPassword,board.getPassword());
    }
}


